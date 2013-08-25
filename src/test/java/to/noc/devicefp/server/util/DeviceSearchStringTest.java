/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import static org.junit.Assert.*;
import org.junit.Test;


public class DeviceSearchStringTest {

    @Test
    public void test_hasJs() {
        String search = "user@example.com hasJs";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertTrue(dss.hasJs());
    }

    @Test
    public void test_hasJs_caseInsensitive() {
        String search = "hAsJS";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertTrue(dss.hasJs());
    }

    @Test
    public void test_hasJs_negative() {
        String search = "user@example.com";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertFalse(dss.hasJs());
    }

    @Test
    public void test_parseDeviceId_simple() {
        String search = "1234";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertEquals(new Long(1234), dss.parseDeviceId());
    }

    @Test
    public void test_parseDeviceId_complex() {
        String search = "192.168.1.1  99999 12345-1234-1334";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertEquals(new Long(99999), dss.parseDeviceId());
    }

    @Test
    public void test_parseDeviceId_negative() {
        String search = "192.168.1 10.0.0.100 12345-1234-1334";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertNull(dss.parseDeviceId());
    }

    @Test
    public void test_parseIpAddress() {
        String search = "x@y.com 192.168.0.1";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertEquals("192.168.0.1", dss.parseIpAddress());
    }

    @Test
    public void test_parseIpAddress_negative() {
        String search = "192.168.1";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertNull(dss.parseIpAddress());
    }

    @Test
    public void test_parseCookie() {
        String search = "user@example.com 0a12b3c4-56de-7f89-01a2-34b5cd67e8f9 192.168.0.1";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertEquals("0a12b3c4-56de-7f89-01a2-34b5cd67e8f9", dss.parseCookieId());
    }

    @Test
    public void test_parseCookie_negative() {
        String search = "0a12b3c4-56de-7f89";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertNull(dss.parseCookieId());
    }

    @Test
    public void test_parseEmail() {
        String search = "user @example.com user@ user@example.com lalala";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertEquals("user@example.com", dss.parseEmail());
    }

    @Test
    public void test_parseEmail_negative() {
        String search = "user @example.com lalala";
        DeviceSearchString dss = new DeviceSearchString(search);
        assertNull(dss.parseEmail());
    }
}
