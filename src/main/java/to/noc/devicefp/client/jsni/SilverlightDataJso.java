/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.SilverlightDataCs;

public final class SilverlightDataJso extends JavaScriptObject implements SilverlightDataCs {

    /* Removing the multiple levels of indirection does now work.  "Content"
     * and "DeviceInfo" are managed Silverlight objects, not true javascript
     * objects.
     */
    protected SilverlightDataJso() {}

    @Override
    public native String getOsVersion() /*-{
        try {
            return this.Content.DeviceInfo.OsVersion;
        } catch(ex) {
            return null;
        }
    }-*/;

    @Override
    public native String getClrVersion() /*-{
        try {
            return this.Content.DeviceInfo.ClrVersion;
        } catch(ex) {
            return null;
        }
    }-*/;

    @Override
    public native String getAssemblyClrVersion() /*-{   
        try {
            return this.Content.DeviceInfo.AssemblyClrVersion;
        } catch(ex) {
            return null;
        }
    }-*/;

    @Override
    public native Integer getProcessorCount() /*-{       
        try {
            return @java.lang.Integer::valueOf(I)(this.Content.DeviceInfo.ProcessorCount);
        } catch(ex) {
            return null;
        }
    }-*/;

    @Override
    public native Integer getSysUptimeMs() /*-{
        try {
            return @java.lang.Integer::valueOf(I)(this.Content.DeviceInfo.SysUptimeMs);
        } catch(ex) {
            return null;
        }
    }-*/;
    
    @Override
    public native Boolean getIsolatedStorageEnabled() /*-{
        try {
            return @java.lang.Boolean::valueOf(Z)(this.Content.DeviceInfo.IsolatedStorageEnabled);
        } catch(ex) {
            return null;
        }
    }-*/;
    
    @Override
    public native Boolean getIsolatedStorageTest() /*-{
        try {
            return @java.lang.Boolean::valueOf(Z)(this.Content.DeviceInfo.IsolatedStorageTest);
        } catch(ex) {
            return null;
        }
    }-*/;
    
    
    public native String getCookieId() /*-{
        try {
            return this.Content.DeviceInfo.getCookie();
        } catch(ex) {
            return null;
        }
    }-*/;
    
    public native boolean setCookieId(String value) /*-{
        try {
            return this.Content.DeviceInfo.setCookie(value);
        } catch(ex) {
            return false;
        }
    }-*/;
}
