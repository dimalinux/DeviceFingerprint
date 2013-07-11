/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;
import static to.noc.devicefp.server.util.IpUtil.ipWithHostName;


@Controller
public class HttpErrorController {
    private static final Logger log = LoggerFactory.getLogger(HttpErrorController.class);

    @RequestMapping("/serverError")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public String handle500(
            HttpServletRequest request,
            @RequestHeader(value="Referer", defaultValue="")
            String referer,
            @RequestHeader(value="Host", defaultValue="")
            String requestHost,
            Map<String, Object> model
            ) {
        String remoteIp = ipWithHostName(request.getRemoteAddr());
        String requestUri = buildPreErrorUri(request);

        Throwable ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
        if (errorMessage == null) {
            errorMessage = (ex != null) ? ex.getMessage() : "unknown error";
        }

        log.warn("Error 500: reqHost={} url={}  referer={} remoteIp={}",
                requestHost, requestUri, referer, remoteIp);
        log.error(errorMessage, ex);

        model.put("homePage", computeHomePage(requestHost));
        return "serverError";
    }


    @RequestMapping("/pageNotFoundError")
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public String handle404(
            HttpServletRequest request,
            @RequestHeader(value="Referer", defaultValue="")
            String referer,
            @RequestHeader(value="Host", defaultValue="")
            String requestHost,
            @RequestHeader(value="X-GWT-Permutation", required=false)
            String xGwtPermutation,
            Map<String, Object> model
            ) {
        String remoteIp = ipWithHostName(request.getRemoteAddr());
        String requestUri = buildPreErrorUri(request);
        boolean isGwtRequest = xGwtPermutation != null;

        String logMsg = "Error 404: reqHost={} url={}  referer={} remoteIp={} gwtRequest={}";
        Object logParams[] = {requestHost, requestUri, referer, remoteIp, isGwtRequest};

        if (isGwtRequest || requestUri.startsWith("/deviceinfo/")) {
            log.error(logMsg, logParams);
        } else {
            log.warn(logMsg, logParams);
        }

        model.put("homePage", computeHomePage(requestHost));
        model.put("requestedPage", HtmlUtils.htmlEscape(requestUri));
        return "pageNotFoundError";
    }

    /*
     * Steals requests that would otherwise go to the 404 handler above.
     * Bots are always requesting these pages.  This is an experiment to
     * see if returning 410 (GONE) reduces repeat requests over time.
     * "method" explicitly set to include HEAD requests (jetty will strip
     * the response body if it's a HEAD request).
     */
    @RequestMapping(
            value={"phpbb3/*", "phpBB3/*", "aboutmembers/*"},
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD}
            )
    @ResponseStatus(HttpStatus.GONE)  // 410
    public String handlePagesPossiblyFromPreviosDomainOwner(
            HttpServletRequest request,
            @RequestHeader(value = "Referer", defaultValue = "") String referer,
            Map<String, Object> model) {

        String remoteIp = ipWithHostName(request.getRemoteAddr());
        String requestUrl = buildFullRequestUrl(request);

        log.warn("Error 410: reqUrl={} referer={} remoteIp={}",
                requestUrl, referer, remoteIp);

        model.put("requestedPage", HtmlUtils.htmlEscape(request.getRequestURI()));
        return "pageGoneForeverError";
    }

    private static String buildPreErrorUri(final HttpServletRequest request) {
        StringBuilder requestUri = new StringBuilder();
        Object errorRequestUri = request.getAttribute("javax.servlet.error.request_uri");
        if (errorRequestUri != null) {
            requestUri.append(errorRequestUri);
        } else {
            requestUri.append(request.getRequestURI());
        }
        String queryParams = request.getQueryString();
        if (queryParams != null) {
            requestUri.append("?").append(queryParams);
        }
        return requestUri.toString();
    }

    private static String buildFullRequestUrl(final HttpServletRequest request) {
        StringBuffer requestUrl = request.getRequestURL();
        String queryParams = request.getQueryString();
        if (queryParams != null) {
            requestUrl.append("?").append(queryParams);
        }
        return requestUrl.toString();
    }

    private static String computeHomePage(String requestHost) {
        String homePage = "/";
        requestHost = requestHost.toLowerCase();
        if (requestHost.contains("dyn.")) {
            // If someone tries to access anything other than "/dns" via a dynamic (wildcard) hostname,
            // we want to give a stripped down hostname for the homepage instead of a relative link.
            homePage = new StringBuilder("http://").append(requestHost.replaceFirst(".*dyn\\.", "")).toString();
        }
        return homePage;
    }
}
