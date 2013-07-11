/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.PluginCs;

public final class PluginJso extends JavaScriptObject implements PluginCs {

    protected PluginJso() {}
   
    @Override
    public native String getName() /*-{
        return this.name;
    }-*/;

    @Override
    public native String getVersion() /*-{
        return this.version;
    }-*/;
    
    @Override
    public native String getDescription() /*-{
        return this.description;
    }-*/;
        
    @Override
    public native String getFilename() /*-{
        return this.filename;
    }-*/;
}