/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.Plugin;

@ProxyFor(value = Plugin.class)
public interface PluginProxy extends ValueProxy, PluginCs {
    public void setName(String name);
    public void setVersion(String version);
    public void setDescription(String description);
    public void setFilename(String filename);
}
