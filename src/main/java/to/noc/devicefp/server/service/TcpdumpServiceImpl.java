/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.domain.entity.DnsData;
import to.noc.devicefp.server.domain.entity.IpPacket;
import to.noc.devicefp.server.domain.entity.TcpSynData;


/*
 * TcpdumpServiceImpl:
 *
 *  Invokes tcpdump listening for TCP SYN packets to the webserver as well
 *  as DNS query packets to the local DNS server.
 *
 *
 *  IP, TCP, UDP and DNS headers are all included below to make our BPF
 *  (Berkeley Packet Filter) expressions understandable:
 *
 *  IP Header Format
 *  ----------------
 *    TCP SYN and simple DNS (UDP) queries are all tiny packets and will
 *    never be fragmented unless someone is fraking with us.  The
 *    following will only accept unfragmented packets or the first
 *    packet in a fragmented series (i.e. ensure "Fragment Offset" is
 *    zero):
 *
 *      (ip[6:2] & 0x1fff) == 0
 *
 *    0                   1                   2                   3
 *    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |Version|  IHL  |Type of Service|          Total Length         |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |         Identification        |Flags|      Fragment Offset    |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |  Time to Live |    Protocol   |         Header Checksum       |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                       Source Address                          |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                    Destination Address                        |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                    Options                    |    Padding    |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *
 *  TCP Header Format:
 *  ------------------
 *    BPF provides built in constants for the TCP flags offset and for
 *    the individual flag masks.  We use the following expression to
 *    only collect TCP syn packets:
 *
 *      (tcp[tcpflags] & (tcp-syn|tcp-ack)) == tcp-syn
 *
 *    0                   1                   2                   3
 *    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |          Source Port          |       Destination Port        |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                        Sequence Number                        |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                    Acknowledgment Number                      |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |  Data |           |U|A|P|R|S|F|                               |
 *   | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
 *   |       |           |G|K|H|T|N|N|                               |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |           Checksum            |         Urgent Pointer        |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                    Options                    |    Padding    |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                             data                              |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *
 *
 *  UDP header:
 *  -----------
 *
 *    0      7 8     15 16    23 24    31
 *   +--------+--------+--------+--------+
 *   |     Source      |   Destination   |
 *   |      Port       |      Port       |
 *   +--------+--------+--------+--------+
 *   |                 |                 |
 *   |     Length      |    Checksum     |
 *   +--------+--------+--------+--------+
 *   |                                   |
 *   |              DATA ...             |
 *   +-----------------------------------+
 *
 *
 *  DNS packet header:
 *  ------------------
 *  Starts after 8 bytes of UDP header.  A forward lookup has QR=0 [query] and
 *  Opcode=0 [standard query], so our packet filter expression is:
 *
 *      (udp[10] & 0xF8) == 0
 *                                   1  1  1  1  1  1
 *     0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                      ID                       |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                    QDCOUNT                    |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                    ANCOUNT                    |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                    NSCOUNT                    |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 *   |                    ARCOUNT                    |
 *   +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 */
@Service
public class TcpdumpServiceImpl implements TcpdumpService {

    private static final Logger log = LoggerFactory.getLogger(TcpdumpServiceImpl.class);

    @Value("${packet.capture.iface}")
    private String packetCaptureIface; // most likely eth0

    @Value("${packet.capture.ip}")
    private String packetCaptureIp;

    @Value("${production.port}")
    private Integer productionPort;

    private Cache<String, DnsData> dnsCache = CacheBuilder.newBuilder().
            initialCapacity(10000).
            maximumSize(100000).
            expireAfterWrite(3, TimeUnit.MINUTES).
            build();

    private Cache<String, TcpSynData> synCache = CacheBuilder.newBuilder().
            initialCapacity(10000).
            maximumSize(100000).
            expireAfterWrite(3, TimeUnit.MINUTES).
            build();

    // Example ipHdrPattern:
    // 1338702946.851823 IP (tos 0x0, ttl 64, id 54655, offset 0, flags [DF], proto TCP (6), length 60)
    // 1360654990.236555 IP (tos 0x2,ECT(0), ttl 112, id 12313, offset 0, flags [DF], proto TCP (6), length 48)
    protected static final Pattern ipHdrPattern = Pattern.compile(
            "([0-9.]+) " +                          // group(1) is timestamp
            "IP \\(tos 0x([0-9a-f]{1,2}),[^ ]* " +  // group(2) is tos
            "ttl (\\d+), " +                        // group(3) is ttl
            "id (\\d+), " +                         // group(4) is IP header ID
            "offset 0, " +
            "flags \\[([^]]+)\\], " +               // group(5) is IP header flags
            "proto (TCP|UDP) \\(\\d+\\), " +        // group(6) is UDP or TCP
            "length (\\d+)\\)"                      // group(7) is length
            );
    private static final int IP_TOS_GROUP       = 2;
    private static final int IP_TTL_GROUP       = 3;
    private static final int IP_ID_GROUP        = 4;
    private static final int IP_FLAGS_GROUP     = 5;
    private static final int IP_PROTO_GROUP     = 6;
    private static final int IP_LENGTH_GROUP    = 7;


    private static final String srcDstRegex = " *(\\d+\\.\\d+\\.\\d+\\.\\d+)\\.(\\d+) > [^:]+: ";

    private static final int PROTO_SRC_IP_GROUP   = 1;
    private static final int PROTO_SRC_PORT_GROUP = 2;

    // Example dnsPattern:
    //    192.168.0.14.58361 > 192.168.0.1.53: 32893+ A? xyz.dyn.noc.to. (33)
    //    198.36.160.1.56610 > 10.248.14.166.53: 8690 [1au] A? 97-115-115-11-38365-20546.dyn.noc.to. (65)
    //    198.36.160.1.65239 > 10.248.14.166.53: 2977% [1au] AAAA? hostname.dyn.noc.to. (43)
    // Since the DNS patter depends on the injected productionHost value, we
    // delay the intialization until productionHost is injected.
    //
    private Pattern dnsPattern;
    private void setDnsPattern() {
        dnsPattern = Pattern.compile(
            srcDstRegex +                               // group(1,2) src ip, src port
            "\\d+[^ ]* (?:\\[[^]]+] )?(A|AAAA)\\? " +   // group(3) A=IPv4, AAAA=IPv6
            "([0-9-]+)\\.[dD][yY][nN]\\..*"             // group(4) host portion of FQDN queried
            );
    }
    private static final int DNS_QTYPE_GROUP    = 3;
    private static final int DNS_HOST_GROUP     = 4;

    // Example tcpSynPattern:
    // 192.168.0.14.50074 > 77.95.133.6.80: Flags [S], cksum 0x934a (incorrect -> 0xe812), seq 4281704785, win 14600, options [mss 1460,sackOK,TS val 64301954 ecr 0,nop,wscale 7], length 0
    private Pattern tcpSynPattern = Pattern.compile(
               srcDstRegex +                    // group(1,2) src ip, src port
               "Flags \\[([A-Za-z]+)\\], " +    // group(3) tcp flags
               "cksum [^,]+, " +
               "seq (\\d+), " +                 // group(4) tcp sequence number
               "win (\\d+), " +                 // group(5) window
               "options \\[([^]]+)\\], " +      // group(6) TCP options
                "length (\\d+)"                 // group(7) length
               );
    private static final int TCP_FLAGS_GROUP    = 3;
    private static final int TCP_SEQ_GROUP      = 4;
    private static final int TCP_WINDOW_GROUP   = 5;
    private static final int TCP_OPTS_GROUP     = 6;
    private static final int TCP_LENGTH_GROUP   = 7;


    private static void setIPHdr(IpPacket ipHdr, Matcher ipHdrMatch, Matcher protoMatcher) {
        try {
            ipHdr.setTos(Short.parseShort(ipHdrMatch.group(IP_TOS_GROUP), 16));
            ipHdr.setTtl(Short.parseShort(ipHdrMatch.group(IP_TTL_GROUP)));
            ipHdr.setIpId(Integer.parseInt(ipHdrMatch.group(IP_ID_GROUP)));
            ipHdr.setIpFlags(ipHdrMatch.group(IP_FLAGS_GROUP));
            ipHdr.setLength(Integer.parseInt(ipHdrMatch.group(IP_LENGTH_GROUP)));
            ipHdr.setSourceIp(protoMatcher.group(PROTO_SRC_IP_GROUP));
            ipHdr.setSourcePort(Integer.parseInt(protoMatcher.group(PROTO_SRC_PORT_GROUP)));
        } catch (NumberFormatException ex) {
            log.error("", ex);
        }
    }

    private static TcpSynData createSynPacket(Matcher ipHdrMatch, Matcher tcpMatcher) {
        TcpSynData synPacket = new TcpSynData();
        setIPHdr(synPacket, ipHdrMatch, tcpMatcher);

        synPacket.setSequenceNum(Long.parseLong(tcpMatcher.group(TCP_SEQ_GROUP)));
        synPacket.setWindowSize(Integer.parseInt(tcpMatcher.group(TCP_WINDOW_GROUP)));
        synPacket.setTcpFlags(tcpMatcher.group(TCP_FLAGS_GROUP));
        synPacket.setTcpOptions(tcpMatcher.group(TCP_OPTS_GROUP));
        synPacket.setTcpLength(Integer.parseInt(tcpMatcher.group(TCP_LENGTH_GROUP)));

        return synPacket;
    }

    private DnsData cacheDnsQueryPacket(Matcher ipHdrMatch, Matcher dnsMatcher) {
        String hostKey = dnsMatcher.group(DNS_HOST_GROUP);
        DnsData dnsData = dnsCache.getIfPresent(hostKey);
        if (dnsData != null) {
            // Synchronize on the dnsData entry to ensure that changes to it
            // are seen as all-or-nothing.
            synchronized (dnsData) {
                if ("AAAA".equals(dnsMatcher.group(DNS_QTYPE_GROUP))) {
                    dnsData.setIpv6RequestMade(true);
                }

                if (dnsData.getSourceIp() == null) {
                    setIPHdr(dnsData, ipHdrMatch, dnsMatcher);
                }
            }
        }

        return dnsData;
    }

    private String createBpf() {

        StringBuilder expr = new StringBuilder();

        expr.append("dst host ").append(packetCaptureIp);
        expr.append(" and ");
        expr.append("(ip[6:2] & 0x1fff) == 0");  // first packet or not fragmented
        expr.append(" and ");
        expr.append("(");
        expr.append(   "(");
        expr.append(      "tcp dst port ").append(productionPort);
        expr.append(      " and ");
        expr.append(      "(tcp[tcpflags] & (tcp-syn|tcp-ack)) == tcp-syn");
        expr.append(   ")");
        expr.append(   " or ");
        expr.append(   "(");
        expr.append(       "udp dst port 53");
        expr.append(       " and ");
        expr.append(       "(udp[10] & 0xF8) == 0"); // dns query
        expr.append(   ")");
        expr.append(")");

        return expr.toString();
    }

    private Process invokeTcpdump() throws IOException {

        String[] cmd = {
            "/usr/sbin/tcpdump",
            "-nn", // use numeric ip addresses and ports
            "-l", // line buffer output
            "-tt", // print unformatted timestamp on each line
            "-v",
            "-S", // print absolute, not relative sequence numbers
            "-p", // don't put the interface into promiscuous mode
            "-s", // snaplen
            "255", // default snaplen is 65535, 255 should be plenty for DNS
            "-i", // interface
            packetCaptureIface,
            createBpf()
        };
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);

        log.info("Launching: {}", Joiner.on(" ").join(cmd));
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private void tcpdumpReadLoop(Process tcpdumpProcess) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(tcpdumpProcess.getInputStream()));

        String ipLine = reader.readLine();
        if (ipLine != null) {
            log.info("STARTED: " + ipLine);

            while ((ipLine = reader.readLine()) != null) {
                Matcher ipHdrMatcher = ipHdrPattern.matcher(ipLine);
                if (!ipHdrMatcher.matches()) {
                    log.error("found something other than ip header line: {}", ipLine);
                    continue;
                }

                String ipProtocol = ipHdrMatcher.group(IP_PROTO_GROUP);
                boolean wasTcp = "TCP".equals(ipProtocol);
                boolean wasUdp = "UDP".equals(ipProtocol);

                String protoLine = reader.readLine();
                if (protoLine == null) {
                    break;
                }

                if (wasUdp) {
                    Matcher dnsMatcher = dnsPattern.matcher(protoLine);
                    if (dnsMatcher.matches()) {
                        DnsData dnsData = cacheDnsQueryPacket(ipHdrMatcher, dnsMatcher);
                        if (dnsData != null) {
                            log.debug("DNS matched packet: {}", dnsData);
                        } else {
                            log.debug("DNS matched packet not in cache: {}", protoLine);
                        }
                    } else {
                        // next line is noisy in test environments using dnsmasq
                        log.debug("DNS line did not match regex: {}", protoLine);
                    }
                } else if (wasTcp) {
                    Matcher synMatcher = tcpSynPattern.matcher(protoLine);
                    if (synMatcher.matches()) {
                        TcpSynData synPacket = createSynPacket(ipHdrMatcher, synMatcher);
                        log.debug("SYN matched packet: {}", synPacket);
                        String synKey = synCacheKey(synPacket.getSourceIp(), synPacket.getSourcePort());
                        synCache.put(synKey, synPacket);
                    } else {
                        log.warn("TCP SYN line did not match regex: {}", protoLine);
                    }
                } else {
                    log.error("This code should be unreachable: {}", ipLine);
                }
            }
        }
    }


    private class Daemon extends Thread {
        private Process tcpdumpProcess;

        public Daemon() {
            setDaemon(true);
            log.debug("Tcpdump Daemon priority before adjustment: {}", getPriority());
            setPriority(Thread.MAX_PRIORITY);
            log.debug("Tcpdump Daemon priority after adjustment: {}", getPriority());
        }

        @Override
        public void run() {
            try {
                tcpdumpProcess = invokeTcpdump();
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        tcpdumpProcess.destroy();
                    }
                });
                tcpdumpReadLoop(tcpdumpProcess);
            } catch (IOException ex) {
                log.error("TcpdumpService tcpdump thread failed", ex);
            }
        }
    }


    @PostConstruct
    public void runTcpDump() {
        setDnsPattern();
        new Daemon().start();
    }


    private static String synCacheKey(String srcIp, int srcPort) {
        return new StringBuilder(srcIp).append('/').append(srcPort).toString();
    }


    private String dnsCacheKey(Device device) {
        return new StringBuilder(device.getIpAddress().replace('.', '-'))
                    .append('-').append(device.getRemotePort())
                    .append('-').append(device.getId())
                    .toString();
    }

    @Override
    public String createHostKeyEntry(Device device) {
        // store an empty entry. packet captures do not create entries, they
        // only update entries that have been explicitly marked for tracking
        // by this method.
        DnsData dnsData = new DnsData();
        String hostKey = dnsCacheKey(device);
        dnsCache.put(hostKey, dnsData);
        log.info("added key '{}' to dns cache", hostKey);
        return hostKey;
    }

    @Override
    public DnsData getDnsData(Device device, String requestedDnsKey) {
        boolean tryBothKeys = false;
        String currentDnsKey = dnsCacheKey(device);
        if (!currentDnsKey.equals(requestedDnsKey)) {
            tryBothKeys = true;
            log.warn("Requested DNS key={} differs from current device key={}", requestedDnsKey, currentDnsKey);
        }

        DnsData dnsData = getDnsData(currentDnsKey);
        if (dnsData == null && tryBothKeys) {
            dnsData = getDnsData(requestedDnsKey);
        }
        return dnsData;
    }

    private DnsData getDnsData(String hostKey) {
        Thread.yield();
        DnsData dnsData = dnsCache.getIfPresent(hostKey);

        if (dnsData != null) {
            // We use a synchronization block to make sure we either have all
            // of the fields or none of them (i.e. not a half processed entry).
            synchronized(dnsData) {
                if (dnsData.getSourceIp() == null) {
                    return null;
                }
            }
        }

        return dnsData;
    }

    @Override
    public TcpSynData getTcpSynData(String srcIp, int srcPort) {
        Thread.yield();
        return synCache.getIfPresent(synCacheKey(srcIp, srcPort));
    }

}
