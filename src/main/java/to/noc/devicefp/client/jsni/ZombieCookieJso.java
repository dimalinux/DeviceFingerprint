/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import java.util.Date;
import to.noc.devicefp.client.entity.ZombieCookieCs;

public final class ZombieCookieJso extends JavaScriptObject implements ZombieCookieCs {

    protected ZombieCookieJso() {}

    @Override
    public native String getId() /*-{
        return this.id;
    }-*/;

    private native double getInceptionMs() /*-{
        return this.inception;
    }-*/;

    @Override      
    public Date getInception() {
        return new Date((long)getInceptionMs());
    }
}