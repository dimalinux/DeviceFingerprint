/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Device implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Integer version;

    @Size(max=45) // 45 = max string length of IPV6 address
    @NotNull
    @Column(length=45, nullable = false)
    @Index(name = "device_ip_idx")
    private String ipAddress;

    @Size(max=255)
    @Column(length=255)
    private String remoteHost;

    private Integer remotePort;

    @Size(max=45) // 45 = max string length of IPV6 address
    @Column(length=45)
    private String proxiedIp;

    @Size(max=255)
    @Column(length=255)
    private String proxiedHost;

    @Size(max=255)
    @Column(length=255, name="rqst_host")
    private String requestHost;

    @ManyToOne(cascade=CascadeType.ALL)
    @NotNull
    @JoinColumn(name="cookie_id", nullable=false)
    @ForeignKey(name="fk_dev_to_cookie")
    private ZombieCookie zombieCookie;

    @Embedded
    private CookieStates cookieStates;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(nullable=false)
    private Date serverStamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date serverEndStamp;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private UserAgentData userAgentData;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private JsData jsData;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private DisplayData displayData;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private JavaData javaData;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private FlashData flashData;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private SilverlightData silverlightData;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private BrowserLocation browserLocation;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private ReverseGeocode reverseGeocode;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private TcpSynData tcpSynData;

    @OneToOne(cascade=CascadeType.ALL, mappedBy="device", optional=true)
    private DnsData dnsData;

    @ManyToOne // No cascade, maxMindLocation persisted seperate from devices
    @ForeignKey(name="fk_dev_to_maxmind")
    @JoinColumn(name="mm_id")
    private MaxMindLocation maxMindLocation;

    private String sessionId;
    private boolean sessionIsNew;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionCreateStamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionLastAccessStamp;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="device")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private List<RequestHeader> requestHeaders = new ArrayList<>();

    @OneToMany(cascade=CascadeType.ALL, mappedBy="device")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private List<Plugin> plugins = new ArrayList<>();

    private boolean markedDeleted;

    public Device(Date serverStamp, String ipAddress) {
        this.serverStamp = serverStamp;
        this.ipAddress = ipAddress;
    }

    public Device() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public String getProxiedIp() {
        return proxiedIp;
    }

    public void setProxiedIp(String proxiedIp) {
        this.proxiedIp = proxiedIp;
    }

    public String getProxiedHost() {
        return proxiedHost;
    }

    public void setProxiedHost(String proxiedHost) {
        this.proxiedHost = proxiedHost;
    }

    public String getRequestHost() {
        return requestHost;
    }

    public void setRequestHost(String requestHost) {
        this.requestHost = requestHost;
    }

    // used for logging
    public String getClientIpInfo() {
        StringBuilder sb = new StringBuilder();
        if (proxiedIp != null) {
            sb.append(proxiedIp);
            if (proxiedHost != null) {
                sb.append("/").append(proxiedHost);
            }
        } else {
            sb.append(ipAddress);
            if (remoteHost != null) {
                sb.append("/").append(remoteHost);
            }
        }
        if (reverseGeocode != null) {
            sb.append("/rg=").append(reverseGeocode.getAddress());
        } else if (maxMindLocation != null) {
            sb.append("/ml=");
            sb.append(maxMindLocation.getAddress());
        }

        return sb.toString();
    }

    public Date getServerStamp() {
        return serverStamp;
    }

    public void setServerStamp(Date serverStamp) {
        this.serverStamp = serverStamp;
    }

    public Date getServerEndStamp() {
        return serverEndStamp;
    }

    public void setServerEndStamp(Date serverEndStamp) {
        this.serverEndStamp = serverEndStamp;
    }

    public UserAgentData getUserAgentData() {
        return userAgentData;
    }

    public void setUserAgentData(UserAgentData userAgentData) {
        if (userAgentData != null) {
            userAgentData.setDevice(this);
        }
        this.userAgentData = userAgentData;
    }

    public List<RequestHeader> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(List<RequestHeader> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public void addRequestHeader(String name, String value) {
        RequestHeader header = new RequestHeader(name, value);
        header.setDevice(this);
        requestHeaders.add(header);
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<Plugin> plugins) {
        for (Plugin plugin: plugins) {
            plugin.setDevice(this);
        }
        this.plugins = plugins;
    }

    public JsData getJsData() {
        return jsData;
    }

    public void setJsData(JsData jsData) {
        jsData.setDevice(this);
        this.jsData = jsData;
    }

    public DisplayData getDisplayData() {
        return displayData;
    }

    public void setDisplayData(DisplayData displayData) {
        displayData.setDevice(this);
        this.displayData = displayData;
    }

    public JavaData getJavaData() {
        return javaData;
    }

    public void setJavaData(JavaData javaData) {
        javaData.setDevice(this);
        this.javaData = javaData;
    }

    public FlashData getFlashData() {
        return flashData;
    }

    public void setFlashData(FlashData flashData) {
        flashData.setDevice(this);
        this.flashData = flashData;
    }

    public SilverlightData getSilverlightData() {
        return silverlightData;
    }

    public void setSilverlightData(SilverlightData silverlightData) {
        silverlightData.setDevice(this);
        this.silverlightData = silverlightData;
    }

    public MaxMindLocation getMaxMindLocation() {
        return maxMindLocation;
    }

    public void setMaxMindLocation(MaxMindLocation maxMindLocation) {
        this.maxMindLocation = maxMindLocation;
    }

    public BrowserLocation getBrowserLocation() {
        return browserLocation;
    }

    public void setBrowserLocation(BrowserLocation browserLocation) {
        browserLocation.setDevice(this);
        this.browserLocation = browserLocation;
    }

    public ReverseGeocode getReverseGeocode() {
        return reverseGeocode;
    }

    public void setReverseGeocode(ReverseGeocode reverseGeocode) {
        reverseGeocode.setDevice(this);
        this.reverseGeocode = reverseGeocode;
    }

    public TcpSynData getTcpSynData() {
        return tcpSynData;
    }

    public void setTcpSynData(TcpSynData tcpSynData) {
        tcpSynData.setDevice(this);
        this.tcpSynData = tcpSynData;
    }

    public DnsData getDnsData() {
        return dnsData;
    }

    public void setDnsData(DnsData dnsData) {
        dnsData.setDevice(this);
        this.dnsData = dnsData;
    }

    public boolean isMarkedDeleted() {
        return markedDeleted;
    }

    public void setMarkedDeleted(boolean markedDeleted) {
        this.markedDeleted = markedDeleted;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isSessionIsNew() {
        return sessionIsNew;
    }

    public void setSessionIsNew(boolean sessionIsNew) {
        this.sessionIsNew = sessionIsNew;
    }

    public Date getSessionCreateStamp() {
        return sessionCreateStamp;
    }

    public void setSessionCreateStamp(Date sessionCreateStamp) {
        this.sessionCreateStamp = sessionCreateStamp;
    }

    public Date getSessionLastAccessStamp() {
        return sessionLastAccessStamp;
    }

    public void setSessionLastAccessStamp(Date sessionLastAccessStamp) {
        this.sessionLastAccessStamp = sessionLastAccessStamp;
    }

    public ZombieCookie getZombieCookie() {
        return zombieCookie;
    }

    public void setZombieCookie(ZombieCookie zombieCookie) {
        this.zombieCookie = zombieCookie;
    }

    public boolean isLinkedTo(Device other) {
        return getZombieCookie().isLinkedTo(other.getZombieCookie());
    }

    public boolean isLinkedTo(OpenIdUser user) {
        return getZombieCookie().isLinkedTo(user);
    }

    public CookieStates getCookieStates() {
        return cookieStates;
    }

    public void setCookieStates(CookieStates cookieStates) {
        if (cookieStates != null) {
            cookieStates.setDevice(this);
        }
        this.cookieStates = cookieStates;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Device other = (Device) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Device{"
                + "id=" + id
                + ", version=" + version
                + "}";
    }


}
