/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.TcpSynDataCs;


public final class TcpSynDataJso extends JavaScriptObject implements TcpSynDataCs {

    protected TcpSynDataJso() {}
        
    @Override
    public native int getIpId() /*-{
        return this.ipId;
    }-*/;

    @Override
    public native short getTos() /*-{
        return this.tos;
    }-*/;
    
    @Override
    public native int getLength() /*-{
        return this.length;
    }-*/;
    
    @Override
    public native String getIpFlags() /*-{
        return this.ipFlags;
    }-*/;

    @Override
    public native short getTtl() /*-{
        return this.ttl;
    }-*/;

    private native double getSequenceNumNative() /*-{
        return this.sequenceNum;
    }-*/;
    
    @Override
    public native int getWindowSize() /*-{
        return this.windowSize;
    }-*/;
    
    @Override
    public long getSequenceNum() {
        return (long)getSequenceNumNative();
    }

    @Override
    public native String getTcpOptions() /*-{
        return this.tcpOptions;
    }-*/;

    @Override
    public native String getTcpFlags() /*-{
        return this.tcpFlags;
    }-*/;

    @Override
    public native int getTcpLength() /*-{
        return this.tcpLength;
    }-*/;

}
