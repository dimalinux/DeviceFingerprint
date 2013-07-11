/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import static to.noc.devicefp.shared.CookieDefs.DEVICE_COOKIE_NAME;
import static to.noc.devicefp.server.util.IpUtil.ipWithHostName;

/*
 *  Controller for the stand alone pages that do geodecoding and geolocation.
 */
@Controller
public class GeoController {

    private static final Logger log = LoggerFactory.getLogger(GeoController.class);

    @RequestMapping("/geoloc")
    public String showGeolocationPage(
            @RequestHeader(value = "Host", required = true) String requestHost,
            @CookieValue(value = DEVICE_COOKIE_NAME, defaultValue = "") String deviceCookieId,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
        )
    {
        String sourceIp = ipWithHostName(request.getRemoteAddr());
        log.info("Geolocation page loaded: " +
                        "reqHost={} clientIp={} device={} session={}",
                    requestHost, sourceIp, deviceCookieId, session.getId());
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        return "geoloc";
    }

    @RequestMapping("/geodecode")
    public String showReverseGeocodePage(
            @RequestHeader(value = "Host", required = true) String requestHost,
            @CookieValue(value = DEVICE_COOKIE_NAME, defaultValue = "") String deviceCookieId,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
        )
    {
        String sourceIp = ipWithHostName(request.getRemoteAddr());
        log.info("Reverse geocode page loaded: " +
                        "reqHost={} clientIp={} device={} session={}",
                    requestHost, sourceIp, deviceCookieId, session.getId());
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        return "reverseGeocode";
    }

    @RequestMapping("/geoloc/geoloc.html")
    public void oldGeolocationPageRedirect(HttpServletResponse response) {
        response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
        response.setHeader("Location", "/geoloc");
    }

    @RequestMapping("/geodecode/reverseGeocode.html")
    public void oldReverseGeocodePageRedirect(HttpServletResponse response) {
        response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
        response.setHeader("Location", "/geodecode");
    }
}
