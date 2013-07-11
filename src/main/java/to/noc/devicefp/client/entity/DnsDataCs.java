/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

/* Client-side (Cs) interface for classes holding FlashData */
public interface DnsDataCs {
    public String getSourceIp();
    public String getHostName();
    public boolean getIpv6RequestMade();
    public MaxMindLocationCs getMaxMindLocation();
}
