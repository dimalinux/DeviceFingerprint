/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *  Our admin interface has a search field that can take any combination of
 *  a "hasJs" flag, an IP address, a device cookie id or an email address.
 *
 *  These values are only used for search (verses validation), so we don't
 *  need uber strict patterns.
 */
public class DeviceSearchString {
    private static final Logger log = LoggerFactory.getLogger(DeviceSearchString.class);

    private String search;
    private static final String preMatch = ".*(?:^|\\s)";
    private static final String postMatch = "(?:\\s|$).*";

    private static Pattern hasJsPattern = Pattern.compile(
            "(?i)" + preMatch + "hasJs" + postMatch
    );

    private static Pattern deviceIdPattern = Pattern.compile(
            preMatch + "(\\d+)" + postMatch
    );

    private static Pattern cookiePattern = Pattern.compile(
           preMatch + "((?:[0-9a-f]+-){4}[0-9a-f]+)" + postMatch
    );

    private static Pattern ipV4Pattern = Pattern.compile(
            preMatch + "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})" + postMatch
    );

    private static Pattern emailPattern = Pattern.compile(
            "(?i)" + preMatch + "([a-z0-9._%+-]+@[A-Z0-9.-]+)" + postMatch
    );

    public DeviceSearchString(String search) {
        this.search = search;
    }

    private String parseFromPattern(Pattern pattern) {
        Matcher matcher = pattern.matcher(search);
        return matcher.matches() ? matcher.group(1) : null;
    }

    public boolean hasJs() {
        return hasJsPattern.matcher(search).matches();
    }

    public Long parseDeviceId() {
        String deviceIdString = parseFromPattern(deviceIdPattern);
        Long deviceId = null;
        if (deviceIdString != null) {
            try {
                deviceId = Long.valueOf(deviceIdString);
            } catch (NumberFormatException ex) {
                log.warn("Device ID '{}' failed to parse {}", deviceIdString, ex.getMessage());
            }
        }
        return deviceId;
    }

    public String parseIpAddress() {
        return parseFromPattern(ipV4Pattern);
    }

    public String parseEmail() {
        return parseFromPattern(emailPattern);
    }

    public String parseCookieId() {
        return parseFromPattern(cookiePattern);
    }

}
