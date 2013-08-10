package to.noc.devicefp.server.service;

import java.util.regex.Pattern;

/*
 *  Regular expression constants used by TcpdumpServiceImpl.
 */
public class TcpdumpRegex {

    // Example ipHdrPattern:
    // 1338702946.851823 IP (tos 0x0, ttl 64, id 54655, offset 0, flags [DF], proto TCP (6), length 60)
    // 1360654990.236555 IP (tos 0x2,ECT(0), ttl 112, id 12313, offset 0, flags [DF], proto TCP (6), length 48)
    static final Pattern ipHdrPattern = Pattern.compile(
            "([0-9.]+) " +                          // group(1) is timestamp
            "IP \\(tos 0x([0-9a-f]{1,2}),[^ ]* " +  // group(2) is tos
            "ttl (\\d+), " +                        // group(3) is ttl
            "id (\\d+), " +                         // group(4) is IP header ID
            "offset 0, " +
            "flags \\[([^]]+)\\], " +               // group(5) is IP header flags
            "proto (TCP|UDP) \\(\\d+\\), " +        // group(6) is UDP or TCP
            "length (\\d+)\\)"                      // group(7) is length
            );
    static final int IP_TOS_GROUP       = 2;
    static final int IP_TTL_GROUP       = 3;
    static final int IP_ID_GROUP        = 4;
    static final int IP_FLAGS_GROUP     = 5;
    static final int IP_PROTO_GROUP     = 6;
    static final int IP_LENGTH_GROUP    = 7;


    // We use this srcDstRegex in the dns and tcp syn patterns
    private static final String srcDstRegex = " *(\\d+\\.\\d+\\.\\d+\\.\\d+)\\.(\\d+) > [^:]+: ";
    static final int PROTO_SRC_IP_GROUP   = 1;
    static final int PROTO_SRC_PORT_GROUP = 2;


    // Example dnsPattern:
    //    192.168.0.1.58361 > 192.168.0.5.53: 32893+ A? 192-168-0-10-38365-20546.dyn.noc.to. (41)
    //    192.168.1.1.56610 > 192.168.0.6.53: 8690 [1au] A? 192-168-0-11-65432-12345.dyn.noc.to. (65)
    //    192.168.2.1.65239 > 192.168.0.7.53: 2977% [1au] AAAA? 192-168-0-12-65432-12345.dyn.noc.to. (43)
    //
    static final Pattern dnsPattern = Pattern.compile(
            srcDstRegex +                               // group(1,2) src ip, src port
            "\\d+[^ ]* (?:\\[[^]]+] )?(A|AAAA)\\? " +   // group(3) A=IPv4, AAAA=IPv6
            "([0-9-]+)\\.[dD][yY][nN]\\..*"             // group(4) host portion of FQDN queried
    );
    static final int DNS_QTYPE_GROUP    = 3;
    static final int DNS_HOST_GROUP     = 4;


    // Example tcpSynPattern:
    // 192.168.0.14.50074 > 77.95.133.6.80: Flags [S], cksum 0x934a (incorrect -> 0xe812), seq 4281704785, win 14600, options [mss 1460,sackOK,TS val 64301954 ecr 0,nop,wscale 7], length 0
    // 198.20.69.98.1870 > 10.248.14.166.80: Flags [S], cksum 0x329e (correct), seq 4173222866, win 1024, length 0
    static final Pattern tcpSynPattern = Pattern.compile(
               srcDstRegex +                    // group(1,2) src ip, src port
               "Flags \\[([A-Za-z]+)\\], " +    // group(3) tcp flags
               "cksum [^,]+, " +
               "seq (\\d+), " +                 // group(4) tcp sequence number
               "win (\\d+), " +                 // group(5) window
               "(?:options \\[([^]]+)\\], )?" + // group(6) TCP options
                "length (\\d+)"                 // group(7) length
               );
    static final int TCP_FLAGS_GROUP    = 3;
    static final int TCP_SEQ_GROUP      = 4;
    static final int TCP_WINDOW_GROUP   = 5;
    static final int TCP_OPTS_GROUP     = 6;
    static final int TCP_LENGTH_GROUP   = 7;
}
