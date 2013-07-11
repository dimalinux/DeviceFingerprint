/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.RequestHeaderCs;

public final class RequestHeaderJso extends JavaScriptObject implements RequestHeaderCs {

    protected RequestHeaderJso() {}

    @Override
    public native String getName() /*-{
        return this.name;
    }-*/;

    @Override
    public native String getValue() /*-{
        return this.value;
    }-*/;
}