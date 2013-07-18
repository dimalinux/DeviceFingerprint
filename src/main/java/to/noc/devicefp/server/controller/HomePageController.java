/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
 package to.noc.devicefp.server.controller;

import flexjson.JSONSerializer;
import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import static to.noc.devicefp.server.util.IpUtil.*;
import static to.noc.devicefp.server.util.ETagUtil.*;
import to.noc.devicefp.server.util.StopWatch;
import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.service.CurrentDeviceService;
import to.noc.devicefp.server.service.CurrentUserService;
import to.noc.devicefp.server.service.MaxMindService;
import to.noc.devicefp.server.service.TcpdumpService;
import to.noc.devicefp.server.util.UAParser;
import static to.noc.devicefp.shared.CookieDefs.DEVICE_COOKIE_NAME;
import static to.noc.devicefp.shared.CookieDefs.DEVICE_COOKIE_MAX_AGE_SECONDS;

@Controller
public class HomePageController {

    private static final Logger log = LoggerFactory.getLogger(HomePageController.class);

    public static final String REQUEST_FACTORY_URL = "gwtRequest";

    @Autowired private TcpdumpService tcpdumpService;
    @Autowired private CurrentUserService currentUserService;
    @Autowired private CurrentDeviceService currentDeviceService;
    @Autowired private MaxMindService maxMindService;

    private static final JSONSerializer deviceSerializer = createDeviceSerializer();

    @ResponseStatus( value = HttpStatus.BAD_REQUEST )
    private final static class BadRequestException extends RuntimeException {
    }
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private final static class NotFoundException extends RuntimeException {
    }

    /*  _escaped_fragment_ handling for search engines is performed in
     *  AjaxCrawlController.
     */
    @RequestMapping(value = "/", params = "!_escaped_fragment_", method = RequestMethod.GET)
    public String showLandingPage(
            @CookieValue(value=DEVICE_COOKIE_NAME, required=false)
            String existingCookieId,
            // Spring will return HTTP status BAD_REQUEST(400) if a required header is missing
            @RequestHeader(value="User-Agent", required=true) String userAgent,
            @RequestHeader(value="Host", required=true) String requestHost,
            @RequestHeader(value = "If-None-Match", required = false) String rawEtag,
            @RequestHeader(value="X-Forwarded-For", required=false) String xForwardedFor,
            @RequestHeader(value="X-GWT-Permutation", required=false) String xGwtPermutation,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            Map<String, Object> model
            )
    {
        log.debug("HomePageController.showLandingPage called");
        StopWatch stopWatch = new StopWatch();

        currentUserService.loadUser();

        if (xGwtPermutation != null) {
            log.error("gwt made request to home page; ip={}", request.getRemoteAddr());
            throw new BadRequestException();
        }

        requestHost = requestHost.toLowerCase();
        if (requestHost.contains("dyn.")) {
            // anything but a "/dns" url on our dynamic domain names should give a 404 error
            log.warn("log to homepage via dynamic domain name; ip={}", request.getRemoteAddr());
            throw new NotFoundException();
        }

        String sourceIp = request.getRemoteAddr();
        String remoteHost = ipToHostName(sourceIp);
        if (isBogusSearchEngine(userAgent, remoteHost)) {
            throw new BadRequestException();
        }

        Date now = new Date();
        Device device = new Device(now, sourceIp);

        device.setRemoteHost(remoteHost);
        device.setRemotePort(request.getRemotePort());

        device.setRequestHost(requestHost);
        device.setUserAgentData(UAParser.parse(userAgent));

        Enumeration allHeaderNames = request.getHeaderNames();
        while (allHeaderNames.hasMoreElements()) {
            String name = (String) allHeaderNames.nextElement();
            String value =  request.getHeader(name);
            if (name.equalsIgnoreCase("Cookie")) {
                value = value.replaceFirst("JSESSIONID=[0-9a-z]+", "JSESSIONID=...");
                value = value.replaceFirst("remember-me=[0-9a-zA-Z]+", "remember-me=...");
            }
            device.addRequestHeader(name, value);
        }

        InetAddress xForwardedForClientIp = ipFromXForwardedForString(xForwardedFor);
        String ipForGeolocation = sourceIp;

        if (xForwardedForClientIp != null) {
            String xForwardedForClientIpStr = xForwardedForClientIp.getHostAddress();
            device.setProxiedIp(xForwardedForClientIpStr);
            if (!isPrivateIp(xForwardedForClientIp)) {
                ipForGeolocation = xForwardedForClientIpStr;
                device.setProxiedHost(ipToHostName(xForwardedForClientIp));
            }
        }

        device.setMaxMindLocation(maxMindService.loadLocation(ipForGeolocation));

        device.setSessionId(session.getId());
        device.setSessionIsNew(session.isNew());
        device.setSessionCreateStamp(new Date(session.getCreationTime()));
        device.setSessionLastAccessStamp(new Date(session.getLastAccessedTime()));

        String etagDeviceId = parseEtagCookieId(rawEtag);
        Date etagDate = parseEtagDate(rawEtag);
        device = currentDeviceService.fixateNewDevice(device, existingCookieId, etagDeviceId);

        String cookieId = device.getZombieCookie().getId();
        Cookie responseCookie = new Cookie(DEVICE_COOKIE_NAME, cookieId);
        responseCookie.setMaxAge(DEVICE_COOKIE_MAX_AGE_SECONDS);
        response.addCookie(responseCookie);

        String dnsKey = tcpdumpService.createHostKeyEntry(device);
        model.put("dnsUrl", createDnsUrl(dnsKey, requestHost, session));

        String requestFactoryUrl = appendSessionId(REQUEST_FACTORY_URL, session);
        model.put("clientJs", createClientJs(device, requestFactoryUrl));

        model.put("user", currentUserService);
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("ETag", createEtag(cookieId, now)); // must be quoted in response
        response.setHeader("Cache-Control", "private");

        if (remoteHost != null && remoteHost.endsWith(".googlebot.com")) {
            session.setMaxInactiveInterval(60*60*8); // 8 hrs, googlebot crawls slow
        }

        log.info("showLandingPage done: deviceId={} requestCookie={} requestEtag={} etagStamp={} loadTime={}",
                device.getId(), existingCookieId, etagDeviceId, etagDate, stopWatch);

        return "index";
    }

    private static String createDnsUrl(String hostnameKey, String requestHost, HttpSession session) {
        // Don't create a DNS url if the site was accessed by IP address
        String dnsUrl = null;
        if (requestHost.matches(".*[A-Za-z].*")) {
            StringBuilder sb = new StringBuilder("http://").append(hostnameKey).append(".dyn.").append(requestHost).append("/dns");
            dnsUrl = appendSessionId(sb, session);
        }
        return dnsUrl;
    }

    private static String appendSessionId(StringBuilder baseUrl, HttpSession session) {
        // DNS URLs require an explicit sessionid, because they use a different
        // domain name. response.encodeURL(...), in theory, should always add the
        // sessionid to our gwt request URL when cookies are disabled, but in
        // practice it has issues.  ... so I'm just hard coding it.  :(
        return baseUrl.append(";jsessionid=").append(session.getId()).toString();
    }

    private static String appendSessionId(String baseUrl, HttpSession session) {
        return appendSessionId(new StringBuilder(baseUrl), session);
    }

    /*
     * The resulting javascript is plugged into the home page from jsp.
     */
    private String createClientJs(Device device, String requestFactoryUrl) {
        StringBuilder sb = new StringBuilder();

        sb.append("var requestFactoryUrl = '").append(requestFactoryUrl).append("';\n");

        sb.append("var currentUser = {anonymous:").append(currentUserService.isAnonymous());
        sb.append(",admin:").append(currentUserService.isAdmin()).append("};\n");

        sb.append("var currentDevice = ");
        deviceSerializer.serialize(device, sb);
        sb.append(";\n");

        return sb.toString();
    }

    private static JSONSerializer createDeviceSerializer() {
         return new JSONSerializer().
                 prettyPrint(false).
                 include(
                    "id",
                    "ipAddress",
                    "remoteHost",
                    "remotePort",
                    "proxiedIp",
                    "proxiedHost",
                    "serverStamp",
                    "requestHeaders.name",
                    "requestHeaders.value",
                    "cookieStates.zombieCookie.id",
                    "cookieStates.zombieCookie.inception",
                    "cookieStates.plainCookieState",
                    "cookieStates.etagCookieState",
                    "cookieStates.version",
                    "userAgentData.uaName",
                    "userAgentData.uaFamily",
                    "userAgentData.uaVersion",
                    "userAgentData.uaDevice",
                    "userAgentData.uaIcon",
                    "userAgentData.osName",
                    "userAgentData.osFamily",
                    "userAgentData.osVersion",
                    "userAgentData.osIcon",
                    "maxMindLocation.ipAddress",
                    "maxMindLocation.stamp",
                    "maxMindLocation.latitude",
                    "maxMindLocation.longitude",
                    "maxMindLocation.countryName",
                    "maxMindLocation.regionName",
                    "maxMindLocation.city",
                    "maxMindLocation.metroCode",
                    "maxMindLocation.areaCode",
                    "maxMindLocation.timeZone",
                    "maxMindLocation.continent",
                    "maxMindLocation.postalCode",
                    "maxMindLocation.isp",
                    "maxMindLocation.org",
                    "maxMindLocation.domain",
                    "maxMindLocation.asnum",
                    "maxMindLocation.netSpeed",
                    "maxMindLocation.userType",
                    "maxMindLocation.accuracyRadius",
                    "maxMindLocation.countryConf",
                    "maxMindLocation.cityConf",
                    "maxMindLocation.regionConf",
                    "maxMindLocation.postalConf",
                    "maxMindLocation.error"
                    ).
                 exclude("*");
    }

    private boolean isBogusSearchEngine(String userAgent, String remoteHost) {
        // TBD:  Verify reverse DNS domains for bots claiming to be Baidu, Google
        //       and Bing.  Should this be implemented as a filter?
        return false;
    }
}
