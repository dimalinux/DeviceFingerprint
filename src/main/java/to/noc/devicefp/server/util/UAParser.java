/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.uadetector.OperatingSystem;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import to.noc.devicefp.server.domain.entity.UserAgentData;
import static to.noc.devicefp.server.domain.entity.UserAgentData.MAX_DEVICE_LEN;

/*
 *  Wraps Andre's UADetector library with additional functionality added.
 */
public class UAParser {

    // Known Android Webkit syntax variations:
    //
    // Mozilla/5.0 (Linux; Android 4.2.2; en-ca; SGH-I337M Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Version/1.0 Chrome/18.0.1025.308 Mobile Safari/535.19
    // Mozilla/5.0 (Linux; U; Android 4.2.1; en-ca; Galaxy Nexus Build/JOP40D; CyanogenMod-10.1) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
    // Mozilla/5.0 (Linux; U; Android 4.1.2; en-au; Galaxy Nexus Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
    // Mozilla/5.0 (Linux; U; Android 2.3.6; en-us; LGL35G/V100) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1
    //
    private static Pattern androidDevPattern = Pattern.compile(
            // Matched pattern will include any "Build/..." text.  We strip it later
            // to keep the regular expression comprehendable.
            "Mozilla/5.0 \\(Linux;(?: U;)? Android [0-9,.]+; [a-z-]+; ([^;)]+).*"
    );

    // Know IOS Syntax variations:
    //
    // Mozilla/5.0 (iPod; CPU iPhone OS 6_1_3 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B329 Safari/8536.25
    // Mozilla/5.0 (iPhone; CPU iPhone OS 6_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B141 Safari/8536.25
    // Mozilla/5.0 (iPad; CPU OS 6_1_2 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B146 Safari/8536.25
    // Mozilla/5.0 (iPhone Simulator; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3
    //
    // Chrome on IOS:
    // Mozilla/5.0 (iPad; CPU OS 6_0_2 like Mac OS X; en-us) AppleWebKit/536.26 (KHTML, like Gecko) CriOS/23.0.1271.100 Mobile/10A550 Safari/8536.25 (BF6B11B6-DBCB-4AC1-B7C6-88B0EBF7786A)
    private static Pattern iosDevPattern = Pattern.compile(
            "Mozilla/5.0 \\(([^;)]+).*"
    );

    private static UserAgentStringParser uaParser =
            UADetectorServiceFactory.getCachingAndUpdatingParser();

    public static UserAgentData parse(String uaString) {
        return convert(uaParser.parse(uaString), uaString);
    }


    public static UserAgentData convert(ReadableUserAgent ua, String uaString) {
        UserAgentData uaData = new UserAgentData();

        uaData.setType(unknownToNull(ua.getTypeName()));
        uaData.setUaFamily(unknownToNull(ua.getFamily().toString()));
        uaData.setUaName(unknownToNull(ua.getName()));
        uaData.setUaVersion(emptyToNull(ua.getVersionNumber().toVersionString()));
        uaData.setUaUrl(unknownToNull(ua.getProducerUrl()));
        uaData.setUaCompany(unknownToNull(ua.getProducer()));
        uaData.setUaCompanyUrl(unknownToNull(ua.getProducerUrl()));
        uaData.setUaIcon(unknownToNull(ua.getIcon()));
        uaData.setUaInfoUrl(unknownToNull(ua.getUrl()));

        OperatingSystem os = ua.getOperatingSystem();
        String osFamily = unknownToNull(os.getFamily().toString());
        uaData.setOsFamily(osFamily);
        uaData.setOsName(unknownToNull(os.getName()));
        uaData.setOsVersion(emptyToNull(os.getVersionNumber().toVersionString()));
        uaData.setOsUrl(unknownToNull(os.getUrl()));
        uaData.setOsCompany(unknownToNull(os.getProducer()));
        uaData.setOsCompanyUrl(unknownToNull(os.getProducerUrl()));
        uaData.setOsIcon(unknownToNull(os.getIcon()));

        if (osFamily != null) {
            if (osFamily.equalsIgnoreCase("IOS")) {
                uaData.setUaDevice(parseIosDeviceName(uaString));
            } else if (osFamily.equalsIgnoreCase("ANDROID")) {
                uaData.setUaDevice(parseAndroidDeviceName(uaString));
            }
        }
        
        return uaData;
    }

    // package scope for unit tests
    static String parseIosDeviceName(String uaString) {
        String deviceName = null;
        Matcher m = iosDevPattern.matcher(uaString);
        if (m.matches()) {
            deviceName = m.group(1);
            if (deviceName.length() > MAX_DEVICE_LEN) {
                deviceName = deviceName.substring(0, MAX_DEVICE_LEN);
            }
        }
        return deviceName;
    }

    // package scope for unit tests
    static String parseAndroidDeviceName(String uaString) {
        String deviceName = null;
        Matcher m = androidDevPattern.matcher(uaString);
        if (m.matches()) {
            deviceName = m.group(1).replaceFirst(" Build.*", "");
            if (deviceName.length() > MAX_DEVICE_LEN) {
                deviceName = deviceName.substring(0, MAX_DEVICE_LEN);
            }
        }
        return deviceName;
    }

    private static String unknownToNull(String value) {
        return !"unknown".equalsIgnoreCase(value) ? emptyToNull(value) : null;
    }

    private static String emptyToNull(String value) {
        return (value != null && !value.isEmpty()) ? value : null;
    }
}
