/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="dns")
public class DnsData extends IpPacket implements Serializable {

    private String hostName;
    private boolean ipv6RequestMade;

    @ManyToOne // no cascade needed
    @ForeignKey(name="fk_dns_to_maxmind")
    @JoinColumn(name="mm_id")
    private MaxMindLocation maxMindLocation;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public boolean getIpv6RequestMade() {
        return ipv6RequestMade;
    }

    public void setIpv6RequestMade(boolean ipv6RequestMade) {
        this.ipv6RequestMade = ipv6RequestMade;
    }

    public MaxMindLocation getMaxMindLocation() {
        return maxMindLocation;
    }

    public void setMaxMindLocation(MaxMindLocation maxMindLocation) {
        this.maxMindLocation = maxMindLocation;
    }

    @Override
    public String toString() {
        return "DnsQueryPacket{" +
                "hostName=" + hostName +
                ", ipv6RequestMade=" + ipv6RequestMade +
                ", " + super.toString() +
                '}';
    }
}
