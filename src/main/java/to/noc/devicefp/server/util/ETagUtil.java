/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * We use ETags of the form "[cookieId]/[timestamp]" (Note that ETags start and
 * end with quotes.)  Below are utility routines for creating and parsing these ETags.
 */
public class ETagUtil {

    private static final Logger log = LoggerFactory.getLogger(ETagUtil.class);

    // Because of the quoting, I went with regular expressions
    private static final Pattern cookieIdPattern = Pattern.compile("\"([^/]+)/.*");
    private static final Pattern timestampPattern = Pattern.compile("[^/]+/([^\"]+).*");

    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
     static {
         fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
     }

    private ETagUtil() {} // prevent instantiation

    public static String createEtag(String cookieId, Date timestamp) {
        StringBuilder sb = new StringBuilder("\"");
        sb.append(cookieId);
        sb.append("/");
        sb.append(fmt.format(timestamp));
        sb.append("\"");
        return sb.toString();
    }

    public static String parseEtagCookieId(String etag) {
        String deviceId = null;
        if (etag != null) {
            Matcher matcher = cookieIdPattern.matcher(etag);
            if (matcher.matches()) {
                deviceId = matcher.group(1);
            }
        }
        return deviceId;
    }

    public static Date parseEtagDate(String etag) {
        Date date = null;
        if (etag != null) {
            Matcher matcher = timestampPattern.matcher(etag);
            if (matcher.matches()) {
                try {
                    String dateStr = matcher.group(1);
                    date = fmt.parse(dateStr);
                } catch (ParseException ex) {
                    log.error("etag had date that did not parse: {}", etag);
                }
            }
        }
        return date;
    }

}