/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.controller;

import flexjson.JSONSerializer;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static to.noc.devicefp.server.util.IpUtil.ipWithHostName;
import static to.noc.devicefp.shared.CookieDefs.DEVICE_COOKIE_NAME;
import to.noc.devicefp.server.domain.entity.DnsData;
import to.noc.devicefp.server.domain.entity.TcpSynData;
import to.noc.devicefp.server.service.CurrentDeviceService;

@Controller
public class DnsJsController {

    private static int MAX_RETRIES = 6;
    private static int SLEEP_MILLISECONDS = 250;
    private static final Logger log = LoggerFactory.getLogger(DnsJsController.class);
    private static final JSONSerializer dnsSerializer =
            new JSONSerializer().prettyPrint(false)
                .include(
                    "sourceIp",
                    "hostName",
                    "ipv6RequestMade",
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
                ).exclude("*");

    private static final JSONSerializer tcpSynSerializer =
            new JSONSerializer().prettyPrint(false)
                .include(
                    "ipId",
                    "tos",
                    "length",
                    "ipFlags",
                    "ttl",
                    "sequenceNum",
                    "windowSize",
                    "tcpOptions",
                    "tcpFlags",
                    "tcpLength"
                ).exclude("*");

    @Autowired
    private CurrentDeviceService currentDeviceService;


    @RequestMapping(value = "/dns", method = RequestMethod.GET)
    public String showDnsData(
            @RequestHeader(value = "Host", required = true) String requestHost,
            @CookieValue(value = DEVICE_COOKIE_NAME, defaultValue = "") String deviceCookieId,
            HttpServletRequest request,
            HttpServletResponse response,
            Map<String, Object> model) {

        TcpSynData tcpSynData = null;
        DnsData dnsData = null;

        if (currentDeviceService.hasCurrentDevice()) {
            long deltaMs = System.currentTimeMillis() - currentDeviceService.getPageLoadStamp().getTime();
            log.info("dnsJs loaded after {} ms", deltaMs);
            loadData(requestHost, request);
            tcpSynData = currentDeviceService.getTcpSynData();
            dnsData = currentDeviceService.getDnsData();
        } else {
            log.error("showDnsData called without a current device: " +
                        "reqHost={} clienIp={} devCookie={}",
                    requestHost, ipWithHostName(request.getRemoteAddr()), deviceCookieId);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("var tcpSynData = ");
        if (tcpSynData != null) {
            tcpSynSerializer.serialize(tcpSynData, sb);
        } else {
            sb.append("null");
        }
        sb.append(";\n");

        sb.append("var dnsData = ");
        if (dnsData != null) {
            dnsSerializer.serialize(dnsData, sb);
        } else {
            sb.append("null");
        }
        sb.append(";\n");

        model.put("jsData", sb.toString());
        return "dnsJs";
    }

    private void loadData(String requestHost, HttpServletRequest request) {

        String requestedDnsKey = requestHost.replaceFirst("\\..*", "");

        TcpSynData tcpSynData = currentDeviceService.loadTcpSynData();
        DnsData dnsData = currentDeviceService.loadDnsData(requestedDnsKey);

        // if we're lucky, we exit the method early
        if (tcpSynData != null && dnsData != null) {
            return;
        }


        int dnsRetries = 0;
        int tcpSynRetries = 0;

        for (int i = 0; i < MAX_RETRIES; i++) {

            // maximum wait is SLEEP_MILLISECONDS * MAX_RETRIES
            sleep(SLEEP_MILLISECONDS);

            if (tcpSynData == null) {
                tcpSynRetries++;
                tcpSynData = currentDeviceService.loadTcpSynData();
            }

            if (dnsData == null) {
                dnsRetries++;
                dnsData = currentDeviceService.loadDnsData(requestedDnsKey);
            }

            if (tcpSynData != null && dnsData != null) {
                break;
            }

        }

        log.warn("TcpSynData(retries={}, found={}) DnsData(retries={}, found={}) reqHost={} clienIp={}",
                tcpSynRetries,
                tcpSynData != null,
                dnsRetries,
                dnsData != null,
                requestHost,
                ipWithHostName(request.getRemoteAddr())
                );
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            log.error("Error calling sleep", ex);
        }
    }
}
