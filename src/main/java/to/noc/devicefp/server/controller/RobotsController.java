/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import static to.noc.devicefp.server.util.IpUtil.ipWithHostName;


@Controller
public class RobotsController {

    @Value("${production.host}")
    private String productionHost;

    private static final Logger log = LoggerFactory.getLogger(RobotsController.class);

    /*
     * We want to ban indexing on anything but our primary domain name.  If
     * the client is *not* comming in on noc.to, we serve a robots.txt file that
     * disallows everything.  If they are comming in on a dynamic DNS domain
     * name, we only allow the dns page to be indexed.
     */
    @RequestMapping("/robots.txt")
    public String showRobotsDotText(
            HttpServletRequest request,
            @RequestHeader(value = "Host", defaultValue = "") String requestHost,
            Map<String, Object> model
            )
    {
        String remoteIp = ipWithHostName(request.getRemoteAddr());
        String mapTo = "robotsDisallow";

        // strip port numbers
        requestHost = requestHost.replaceFirst(":.*", "").toLowerCase();
        if (requestHost.equals(productionHost)) {
            mapTo = "robotsAllow";
        } else if (requestHost.endsWith(".dyn." + productionHost)) {
            mapTo = "robotsDnsLinkOnly";
        }

        log.debug("RobotsController called, reqHost={} remoteIp={} mapTo={}",
                requestHost, remoteIp, mapTo);

        return mapTo;
    }
}
