/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import static org.junit.Assert.*;
import org.junit.Test;
import static to.noc.devicefp.server.util.IpUtil.*;

public class IpUtilTest {
    
    @Test
    public void test_ipFromString() {
        assertNull(ipFromString("abc.hij.qpf.xyz"));
        assertNull(ipFromString(null));
        assertTrue(ipFromString("192.168.0.1").getHostAddress().equals("192.168.0.1"));
    }
    
    @Test
    public void test_ipFromXForwardedFor() {
        assertNull(ipFromXForwardedForString("abc.hij.qpf.xyz"));
        assertNull(ipFromXForwardedForString(null));
        assertTrue(ipFromXForwardedForString("192.168.0.1").getHostAddress().equals("192.168.0.1"));
        assertTrue(ipFromXForwardedForString("192.168.0.1 8.8.8.8").getHostAddress().equals("192.168.0.1"));
        assertTrue(ipFromXForwardedForString("192.168.0.1, 8.8.8.8").getHostAddress().equals("192.168.0.1"));
    }
    
    @Test
    public void test_isPrivateIp() {
        assertTrue(isPrivateIp("127.0.0.1"));
        assertTrue(isPrivateIp("0:0:0:0:0:0:0:1"));  // ipv6 loopback
        assertTrue(isPrivateIp("192.168.0.1"));
        assertTrue(isPrivateIp("10.0.0.1"));
        
        // Google DNS
        assertFalse(isPrivateIp("8.8.8.8"));
        
        // Carrier grade NAT:
        // 100.64.0.0/10 (100.64.0.0 – 100.127.255.255)
        assertFalse(isPrivateIp("100.0.0.1"));
        assertFalse(isPrivateIp("100.63.255.254"));        
        assertTrue(isPrivateIp("100.64.0.1"));
        assertTrue(isPrivateIp("100.127.0.1"));
        assertFalse(isPrivateIp("100.128.0.1"));
    }
    
    @Test
    public void test_ipToHostName() {
        assertNull(ipToHostName((String)null));
        assertNull(ipToHostName(""));
        assertNull(ipToHostName("xyz"));
        assertNotNull(ipToHostName("8.8.8.8"));
    }
}
