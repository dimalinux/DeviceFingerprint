/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
 package to.noc.devicefp.server.controller;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 import static to.noc.devicefp.server.util.IpUtil.ipWithHostName;

/*
 * Return a static snapshot (with javascript evaluated) for search engines
 * if a request comes in with a _escaped_fragment_ "parameter.  At the current
 * time, only the homepage has indexable fragments.
 */
@Controller
public class AjaxCrawlController {

    private static final Logger log = LoggerFactory.getLogger(AjaxCrawlController.class);

    // only 3 pages are should be requested by search engines, but we make the
    // cache a little bigger in case there are bad requests. We use a cache,
    // since evaluating the javascript is both computationally expensive and greatly
    // increases page load time.
    private static LoadingCache<String, String> pageCache = CacheBuilder.newBuilder().
            initialCapacity(5).
            maximumSize(20).
            build(
                new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return loadPageBody(key);
                    }
            });

    @RequestMapping(value = "/", params = "_escaped_fragment_", method = RequestMethod.GET)
    public String ajaxCrawl(
            @RequestParam(value = "_escaped_fragment_") String escapedFragment,
            @RequestHeader(value="Host", required=true) String requestHost,
            HttpServletRequest request,
            HttpServletResponse response,
            Map<String, Object> model
            ) {

        log.info("ajaxCrawl called reqHost={} remoteIp={} fragment={}",
                requestHost, ipWithHostName(request.getRemoteAddr()), escapedFragment);

        String url = "http://" + requestHost;
        if (escapedFragment != null && escapedFragment.length() > 0) {
            url += "#!" + escapedFragment;
        }

        model.put("body", lookupOrLoadPageBody(url));
        log.info("ajaxCrawl done: fragment={}", escapedFragment);
        return "ajaxCrawl";
    }

    private static String lookupOrLoadPageBody(String url) {
        String body;
        try {
            // cache will call loadPageBody if needed
            body = pageCache.get(url);
        } catch (ExecutionException ex) {
            log.error("Error loading crawler page", ex);
            body = "<body>Error loading page for crawler</body>";
        }
        return body;
    }

    private static String loadPageBody(String url) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        WebClientOptions options = webClient.getOptions();
        options.setThrowExceptionOnScriptError(false);

        // if an error occurs, we propopate upwards
        try {
            HtmlPage page = webClient.getPage(url);
            log.info("Non cached _esccaped_fragment_ page pulled: {}", url);
            //execute javascript for 4 seconds
            webClient.getJavaScriptEngine().pumpEventLoop(4000);

            return page.getBody().asXml();
        } finally {
            webClient.closeAllWindows();
        }
    }

}
