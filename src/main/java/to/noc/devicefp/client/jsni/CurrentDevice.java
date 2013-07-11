/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import java.util.*;
import to.noc.devicefp.client.entity.*;
import static to.noc.devicefp.shared.ValDisplayUtil.nullToEmpty;


public final class CurrentDevice extends JavaScriptObject implements DeviceCs {
    private static List<RequestHeaderCs> requestHeaders;
    private static List<PluginCs> plugins;
    private static JsDataCs jsData;
    private static CurrentDisplayData displayData = CurrentDisplayData.instance();
    private static JavaDataCs javaData;
    private static FlashDataCs flashData;
    private static SilverlightDataJso silverlightData;
    private static BrowserLocationCs browserLocation;
    private static ReverseGeocodeCs reverseGeocode;
    private static CookieStatesCs cookieStates;

    private static CurrentDevice instance;

    protected CurrentDevice() {}

    private static native CurrentDevice getJso() /*-{
        return $wnd.currentDevice;
    }-*/;

    public static CurrentDevice instance() {
        if (instance == null) {
            instance = getJso();
        }
        return instance;
    }

    @Override
    public Long getId() {
        // just casting would probably be safe, but rounding seemed safer
        return Math.round(getIdNative());
    }

    private native double getIdNative() /*-{
        return this.id;
    }-*/;

    @Override
    public native String getIpAddress() /*-{
        return this.ipAddress;
    }-*/;

    @Override
    public native String getRemoteHost() /*-{
        return this.remoteHost;
    }-*/;

    @Override
    public native Integer getRemotePort() /*-{
        return @java.lang.Integer::valueOf(I)(this.remotePort);
    }-*/;

    @Override
    public native String getProxiedIp() /*-{
        return this.proxiedIp;
    }-*/;

    @Override
    public native String getProxiedHost() /*-{
        return this.proxiedHost;
    }-*/;

    private native double getServerStampMs() /*-{
        return this.serverStamp;
    }-*/;

    @Override
    public  Date getServerStamp() {
        return new Date((long)getServerStampMs());
    }

    private native JsArray<RequestHeaderJso> getRequestHeadersJso() /*-{
        return this.requestHeaders;
    }-*/;

    @Override
    public List<RequestHeaderCs> getRequestHeaders() {
        if (requestHeaders == null) {
            JsArray<RequestHeaderJso> jsoHeaders = getRequestHeadersJso();
            int length = jsoHeaders.length();
            requestHeaders = new ArrayList<RequestHeaderCs>(length);
            for (int i = 0; i < length; i++) {
                requestHeaders.add(jsoHeaders.get(i));
            }
        }
        return requestHeaders;
    }

    @Override
    public native UserAgentDataJso getUserAgentData() /*-{
        return this.userAgentData;
    }-*/;

    @Override
    public List<PluginCs> getPlugins() {
        if (plugins == null) {
            JsArray<PluginJso> pluginsJso = NavigatorFull.getPlugins();
            int length = pluginsJso.length();
            plugins = new ArrayList<PluginCs>(length);
            for (int i = 0; i < length; i++) {
                plugins.add(pluginsJso.get(i));
            }
            Collections.sort(plugins, new Comparator<PluginCs>() {
                @Override
                public int compare(PluginCs lhs, PluginCs rhs) {
                    String leftName = nullToEmpty(lhs.getName());
                    String rightName = nullToEmpty(rhs.getName());
                    return leftName.compareToIgnoreCase(rightName);
                }
            });
        }
        return plugins;
    }


    @Override
    public JsDataCs getJsData() {
        return CurrentDevice.jsData;
    }

    public void setJsData(JsDataCs jsData) {
	CurrentDevice.jsData = jsData;
    }

    @Override
    public DisplayDataCs getDisplayData() {
        return CurrentDevice.displayData;
    }

    @Override
    public JavaDataCs getJavaData() {
        return CurrentDevice.javaData;
    }

    public void setJavaData(JavaDataCs javaData) {
	CurrentDevice.javaData = javaData;
    }

    @Override
    public FlashDataCs getFlashData() {
        return flashData;
    }

    public void setFlashData(FlashDataCs flashData) {
        CurrentDevice.flashData = flashData;
    }

    @Override
    public SilverlightDataJso getSilverlightData() {
        return silverlightData;
    }

    public void setSilverlightData(SilverlightDataJso silverlightData) {
        CurrentDevice.silverlightData = silverlightData;
    }

    @Override
    public BrowserLocationCs getBrowserLocation() {
        return browserLocation;
    }

    public void setBrowserLocation(BrowserLocationCs location) {
        CurrentDevice.browserLocation = location;
    }

    @Override
    public ReverseGeocodeCs getReverseGeocode() {
        return reverseGeocode;
    }

    public void setReverseGeocode(ReverseGeocodeCs reverseGeocode) {
        CurrentDevice.reverseGeocode = reverseGeocode;
    }

    @Override
    public native MaxMindLocationJso getMaxMindLocation() /*-{
        return this.maxMindLocation;
    }-*/;

    @Override
    public native TcpSynDataJso getTcpSynData() /*-{
        return $wnd.tcpSynData;
    }-*/;

    @Override
    public native DnsDataCs getDnsData() /*-{
        return $wnd.dnsData;
    }-*/;

    private native CookieStatesJso getCookieStatesJso() /*-{
        return this.cookieStates;
    }-*/;

    @Override
    public CookieStatesCs getCookieStates() {
        if (cookieStates == null) {
            cookieStates = getCookieStatesJso();
        }
        return cookieStates;
    }

    public void setCookieStates(CookieStatesCs cookieStates) {
         CurrentDevice.cookieStates = cookieStates;
    }

    @Override
    public Date getServerEndStamp() {
        return null; // current device hasn't ended by definition
    }

}
