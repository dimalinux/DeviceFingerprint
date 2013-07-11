/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.domain.entity.DnsData;
import to.noc.devicefp.server.domain.entity.TcpSynData;


public interface TcpdumpService {

    String createHostKeyEntry(Device device);

    DnsData getDnsData(Device device, String requestedDnsKey);

    TcpSynData getTcpSynData(String srcIp, int srcPort);

}
