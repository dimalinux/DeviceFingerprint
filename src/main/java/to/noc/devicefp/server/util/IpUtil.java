/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import com.google.common.net.InetAddresses;
import java.net.InetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtil {

    private static final Logger log = LoggerFactory.getLogger(IpUtil.class);
    private IpUtil() {} // prevent instantiation

    //
    // X-Forwarded-For standard format: client, proxy1, proxy2
    // http://en.wikipedia.org/wiki/X-Forwarded-For
    //
    // If the string starts with an IP address, that address is returned.
    // Othwerwise returns null.
    //
    public static InetAddress ipFromXForwardedForString(String xForwardedFor) {
        return (xForwardedFor != null)
                ? ipFromString(xForwardedFor.split("[^0-9.]", 2)[0]) : null;
    }

    //
    //  Converts a string to an IP address.  If null or an invalid string is
    //  passed, null is retuned.
    //
    public static InetAddress ipFromString(String ipString) {
        InetAddress ip = null;
        if (ipString != null) {
            try {
                ip = InetAddresses.forString(ipString);
            } catch (IllegalArgumentException ex) {
                log.debug("IpUtil.ipFromString passed invalid ip: {}", ipString);
            }
        }
        return ip;
    }

    public static boolean isPrivateIp(String ipString) {
        return isPrivateIp(ipFromString(ipString));
    }

    //
    // We don't have full coverage of the arcane reserved subnets, but local
    // loops, standard private subnets and the newer carrier grade NAT subnet
    // are handled:
    //    http://en.wikipedia.org/wiki/Reserved_IP_addresses
    //
    public static boolean isPrivateIp(InetAddress ip) {
        if (ip.isSiteLocalAddress() || ip.isLoopbackAddress()) {
            return true;
        }

        byte[] octets = ip.getAddress();
        byte oct1 = octets[0];

        // Private IPs used for carrier grade NAT
        if (oct1 == 100) {
            byte octet2 = octets[1];
            if (octet2 >= 64 && octet2 < 128) {
                return true;
            }
        }

        return false;
    }

    //
    //  Given a correctly formated ip address, the reverse
    //  DNS fully qualified hostname is returned.  If there is no
    //  hostname in DNS or the string is null or incorrectly
    //  formated, null is returned.
    //
    public static String ipToHostName(String ipString) {
        return ipToHostName(ipFromString(ipString));
    }

    public static String ipToHostName(InetAddress ip) {
        String hostName = null;
        if (ip != null) {
            hostName = ip.getCanonicalHostName();
            if (hostName.equals(ip.getHostAddress())) {
                hostName = null;
            }
        }
        return hostName;
    }

    // expand "ip" string to "ip/hostname" (for logging)
    public static String ipWithHostName(String ipAddress) {
        String hostName = ipToHostName(ipAddress);
        StringBuilder ipAndHost = new StringBuilder();
        // use append for the ip in case it's null
        return ipAndHost.append(ipAddress).append("/").append(
                        hostName != null ? hostName : "NO_DNS"
                    ).toString();
    }
}