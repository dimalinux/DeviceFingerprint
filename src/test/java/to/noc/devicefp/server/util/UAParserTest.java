/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import static org.junit.Assert.*;
import org.junit.Test;
import to.noc.devicefp.server.domain.entity.UserAgentData;

public class UAParserTest {

    @Test
    public void test_parseAndroidDeviceName_variant1() {
        String uaString = "Mozilla/5.0 (Linux; Android 4.2.2; en-ca; SGH-I337M Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Version/1.0 Chrome/18.0.1025.308 Mobile Safari/535.19";
        assertEquals("SGH-I337M", UAParser.parseAndroidDeviceName(uaString));
    }

    @Test
    public void test_parseAndroidDeviceName_variant2() {
        String uaString = "Mozilla/5.0 (Linux; U; Android 4.2.1; en-ca; Galaxy Nexus Build/JOP40D; CyanogenMod-10.1) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        assertEquals("Galaxy Nexus", UAParser.parseAndroidDeviceName(uaString));
    }

    @Test
    public void test_parseAndroidDeviceName_variant3() {
        String uaString = "Mozilla/5.0 (Linux; U; Android 4.1.2; en-au; Galaxy Nexus Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        assertEquals("Galaxy Nexus", UAParser.parseAndroidDeviceName(uaString));
    }

    @Test
    public void test_parseAndroidDeviceName_variant4() {
        String uaString = "Mozilla/5.0 (Linux; U; Android 2.3.6; en-us; LGL35G/V100) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
        assertEquals("LGL35G/V100", UAParser.parseAndroidDeviceName(uaString));
    }

    @Test
    public void test_parseAndroidDeviceName_variant5() {
        String uaString = "Mozilla/5.0 (Linux; Android 4.0.3; Transformer TF101 Build/IML74K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.64 Safari/537.36";
        assertEquals("Transformer TF101", UAParser.parseAndroidDeviceName(uaString));
    }

    @Test
    public void test_parseAndroidDeviceName_truncate() {
        String uaString = "Mozilla/5.0 (Linux; U; Android 4.1.2; en-au; 01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        assertEquals(UserAgentData.MAX_DEVICE_LEN, UAParser.parseAndroidDeviceName(uaString).length());
    }


    @Test
    public void test_parseIosDeviceName_variant1() {
        String uaString = "Mozilla/5.0 (iPod; CPU iPhone OS 6_1_3 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B329 Safari/8536.25";
        assertEquals("iPod", UAParser.parseIosDeviceName(uaString));
    }

    @Test
    public void test_parseIosDeviceName_variant2() {
        String uaString = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B141 Safari/8536.25";
        assertEquals("iPhone", UAParser.parseIosDeviceName(uaString));
    }

    @Test
    public void test_parseIosDeviceName_variant3() {
        String uaString = "Mozilla/5.0 (iPad; CPU OS 6_1_2 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B146 Safari/8536.25";
        assertEquals("iPad", UAParser.parseIosDeviceName(uaString));
    }

    @Test
    public void test_parseIosDeviceName_variant4() {
        String uaString = "Mozilla/5.0 (iPhone Simulator; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";
        assertEquals("iPhone Simulator", UAParser.parseIosDeviceName(uaString));
    }

    @Test
    public void test_parseIosDeviceName_truncate() {
        String uaString = "Mozilla/5.0 (i01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3";
        assertEquals(UserAgentData.MAX_DEVICE_LEN, UAParser.parseIosDeviceName(uaString).length());
    }

}
