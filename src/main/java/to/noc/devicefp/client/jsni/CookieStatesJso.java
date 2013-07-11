/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.CookieStatesCs;
import to.noc.devicefp.shared.CookieState;

public final class CookieStatesJso extends JavaScriptObject implements CookieStatesCs {

    protected CookieStatesJso() {}

    @Override
    public native CookieState getPlainState() /*-{
        return this.getPlainState;
    }-*/;
        
    @Override
    public native CookieState getEtagState() /*-{
        return this.getEtagState;
    }-*/;

    @Override
    public CookieState getFlashState() {
        return null;
    }

    @Override
    public CookieState getWebStorageState() {
        return null;
    }

    @Override
    public CookieState getSilverlightState() {
        return null;
    }

    @Override
    public native ZombieCookieJso getZombieCookie() /*-{
        return this.zombieCookie;
    }-*/;

    @Override
    public native Integer getVersion() /*-{
        return @java.lang.Integer::valueOf(I)(this.version);
    }-*/;
}
