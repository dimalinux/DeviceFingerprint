package to.noc.devicefp.server.service;

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import static to.noc.devicefp.server.service.TcpdumpRegex.*;

public class TcpdumpRegexTest {

    @Test
    public void test_ipHdrPattern_zeroValueTos() {
        String input = "1338702946.851823 IP (tos 0x0, ttl 64, id 54655, offset 0, flags [DF], proto TCP (6), length 60)";
        Matcher matcher = ipHdrPattern.matcher(input);
        assertTrue(matcher.matches());
        assertEquals("0", matcher.group(IP_TOS_GROUP));
        assertEquals("64", matcher.group(IP_TTL_GROUP));
        assertEquals("54655", matcher.group(IP_ID_GROUP));
        assertEquals("DF", matcher.group(IP_FLAGS_GROUP));
        assertEquals("TCP", matcher.group(IP_PROTO_GROUP));
        assertEquals("60", matcher.group(IP_LENGTH_GROUP));
    }

    @Test
    public void test_ipHdrPattern_withExplicitCongestionNotificationText() {
        String input = "1360654990.236555 IP (tos 0x2,ECT(0), ttl 112, id 12313, offset 0, flags [DF], proto TCP (6), length 48)";
        Matcher matcher = ipHdrPattern.matcher(input);
        assertTrue(matcher.matches());
        assertEquals("2", matcher.group(IP_TOS_GROUP));
        assertEquals("112", matcher.group(IP_TTL_GROUP));
        assertEquals("12313", matcher.group(IP_ID_GROUP));
        assertEquals("DF", matcher.group(IP_FLAGS_GROUP));
        assertEquals("TCP", matcher.group(IP_PROTO_GROUP));
        assertEquals("48", matcher.group(IP_LENGTH_GROUP));
    }


    @Test
    public void test_dnsPattern_ipv4Lookup_variant1() {
        String input = "192.168.0.1.58361 > 192.168.0.5.53: 32893+ A? 192-168-0-10-38365-20546.dyn.noc.to. (41)";
        Matcher matcher = dnsPattern.matcher(input);
        assertTrue(matcher.matches());
        assertEquals("192.168.0.1", matcher.group(PROTO_SRC_IP_GROUP));
        assertEquals("58361", matcher.group(PROTO_SRC_PORT_GROUP));
        assertEquals("A", matcher.group(DNS_QTYPE_GROUP));
        assertEquals("192-168-0-10-38365-20546", matcher.group(DNS_HOST_GROUP));
    }

    @Test
    public void test_dnsPattern_ipv4Lookup_variant2() {
        String input = "192.168.1.1.56610 > 192.168.0.6.53: 8690 [1au] A? 192-168-0-11-65432-12345.dyn.noc.to. (65)";
        Matcher matcher = dnsPattern.matcher(input);
        assertTrue(matcher.matches());
        assertEquals("192.168.1.1", matcher.group(PROTO_SRC_IP_GROUP));
        assertEquals("56610", matcher.group(PROTO_SRC_PORT_GROUP));
        assertEquals("A", matcher.group(DNS_QTYPE_GROUP));
        assertEquals("192-168-0-11-65432-12345", matcher.group(DNS_HOST_GROUP));
    }

    @Test
    public void test_dnsPattern_ipv6IpLookup() {
        String input = "192.168.2.1.65239 > 192.168.0.7.53: 2977% [1au] AAAA? 192-168-0-12-65432-12345.dyn.noc.to. (43)";
        Matcher matcher = dnsPattern.matcher(input);
        assertTrue(matcher.matches());
        assertEquals("192.168.2.1", matcher.group(PROTO_SRC_IP_GROUP));
        assertEquals("65239", matcher.group(PROTO_SRC_PORT_GROUP));
        assertEquals("AAAA", matcher.group(DNS_QTYPE_GROUP));
        assertEquals("192-168-0-12-65432-12345", matcher.group(DNS_HOST_GROUP));
    }
    

    @Test
    public void test_tcpSynPattern_withOptions() {
        String input = "192.168.0.14.50074 > 192.168.0.7.80: Flags [S], cksum 0x934a (incorrect -> 0xe812), seq 4281704785, win 14600, options [mss 1460,sackOK,TS val 64301954 ecr 0,nop,wscale 7], length 0";
        Matcher matcher = tcpSynPattern.matcher(input);
        assertTrue(matcher.matches());
        assertEquals("192.168.0.14", matcher.group(PROTO_SRC_IP_GROUP));
        assertEquals("50074", matcher.group(PROTO_SRC_PORT_GROUP));
        assertEquals("S", matcher.group(TCP_FLAGS_GROUP));
        assertEquals("4281704785", matcher.group(TCP_SEQ_GROUP));
        assertEquals("14600", matcher.group(TCP_WINDOW_GROUP));
        assertEquals("mss 1460,sackOK,TS val 64301954 ecr 0,nop,wscale 7", matcher.group(TCP_OPTS_GROUP));
        assertEquals("0", matcher.group(TCP_LENGTH_GROUP));
    }

    @Test
    public void test_tcpSynPattern_noOptions() {
        String input = "192.168.0.14.1870 > 192.168.0.7.80: Flags [S], cksum 0x329e (correct), seq 4173222866, win 1024, length 0";
        Matcher matcher = tcpSynPattern.matcher(input);
        assertTrue(matcher.matches());
        assertEquals("192.168.0.14", matcher.group(PROTO_SRC_IP_GROUP));
        assertEquals("1870", matcher.group(PROTO_SRC_PORT_GROUP));
        assertEquals("S", matcher.group(TCP_FLAGS_GROUP));
        assertEquals("4173222866", matcher.group(TCP_SEQ_GROUP));
        assertEquals("1024", matcher.group(TCP_WINDOW_GROUP));
        assertEquals(null, matcher.group(TCP_OPTS_GROUP));
        assertEquals("0", matcher.group(TCP_LENGTH_GROUP));
    }
}