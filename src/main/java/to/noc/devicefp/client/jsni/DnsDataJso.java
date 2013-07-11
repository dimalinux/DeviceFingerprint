/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.DnsDataCs;
import to.noc.devicefp.client.entity.MaxMindLocationCs;


public final class DnsDataJso extends JavaScriptObject implements DnsDataCs {
    
    protected DnsDataJso() {}
    
    @Override
    public native String getSourceIp() /*-{
        return this.sourceIp;
    }-*/;

    @Override
    public native String getHostName() /*-{
        return this.hostName;
    }-*/;

    @Override
    public native boolean getIpv6RequestMade() /*-{
        return this.ipv6RequestMade;
    }-*/;
    
    @Override
    public native MaxMindLocationCs getMaxMindLocation() /*-{
        return this.maxMindLocation;
    }-*/;
    
}
