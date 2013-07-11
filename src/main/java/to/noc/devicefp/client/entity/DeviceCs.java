/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import java.util.Date;
import java.util.List;

/* Client-side (Cs) interface for classes holding Device data */
public interface DeviceCs {
    public Long getId();
    public String getIpAddress();
    public String getRemoteHost();
    public Integer getRemotePort();
    public String getProxiedIp();
    public String getProxiedHost();   
    public Date getServerStamp();
    public List<? extends RequestHeaderCs> getRequestHeaders();
    public List<? extends PluginCs> getPlugins();   
    public JsDataCs getJsData();
    public DisplayDataCs getDisplayData();
    public JavaDataCs getJavaData(); 
    public FlashDataCs getFlashData();
    public SilverlightDataCs getSilverlightData();
    public BrowserLocationCs getBrowserLocation();
    public ReverseGeocodeCs getReverseGeocode();
    public MaxMindLocationCs getMaxMindLocation();
    public CookieStatesCs getCookieStates();
    public UserAgentDataCs getUserAgentData();
    public TcpSynDataCs getTcpSynData();
    public DnsDataCs getDnsData();
    public Date getServerEndStamp();
}
