/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.UserAgentDataCs;

public final class UserAgentDataJso extends JavaScriptObject implements UserAgentDataCs {

    protected UserAgentDataJso() {}

    @Override
    public native String getUaName()/*-{
        return this.uaName;
    }-*/;

    @Override
    public native String getUaFamily()/*-{
        return this.uaFamily;
    }-*/;

    @Override
    public native String getUaVersion()/*-{
        return this.uaVersion;
    }-*/;

    @Override
    public native String getUaDevice()/*-{
        return this.uaDevice;
    }-*/;

    @Override
    public native String getUaIcon()/*-{
        return this.uaIcon;
    }-*/;

    @Override
    public native String getOsName()/*-{
        return this.osName;
    }-*/;

    @Override
    public native String getOsFamily()/*-{
        return this.osFamily;
    }-*/;

    @Override
    public native String getOsVersion()/*-{
        return this.osVersion;
    }-*/;

    @Override
    public native String getOsIcon()/*-{
        return this.osIcon;
    }-*/;

}
