/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;

public final class CurrentUser extends JavaScriptObject {

    protected CurrentUser() {}

    private static CurrentUser instance;

    private static native CurrentUser getJso() /*-{
        return $wnd.currentUser;
    }-*/;

    public static CurrentUser instance() {
        if (instance == null)
            instance = getJso();
        return instance;
    }

    public native boolean isAnonymous() /*-{
        return this.anonymous;
    }-*/;

    public native boolean isAdmin() /*-{
        return this.admin;
    }-*/;

}
