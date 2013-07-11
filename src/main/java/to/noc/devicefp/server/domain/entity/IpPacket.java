/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IpPacket extends DeviceFkAsPk {
    public int ipId;
    public String sourceIp;
    public int sourcePort;
    public short tos;
    public int length;
    public String ipFlags;
    public short ttl;
   
    public int getIpId() {
        return ipId;
    }

    public void setIpId(int ipId) {
        this.ipId = ipId;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public short getTos() {
        return tos;
    }

    public void setTos(short tos) {
        this.tos = tos;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getIpFlags() {
        return ipFlags;
    }

    public void setIpFlags(String ipFlags) {
        this.ipFlags = ipFlags;
    }

    public short getTtl() {
        return ttl;
    }

    public void setTtl(short ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "IpPacket{" + "ipId=" + ipId + ", sourceIp=" + sourceIp + ", sourcePort=" + sourcePort + ", tos=" + tos + ", length=" + length + ", ipFlags=" + ipFlags + ", ttl=" + ttl + '}';
    }

}
