/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

/* Client-side (Cs) interface for classes holding Plugin description */
public interface PluginCs  {
    public String getName();
    public String getVersion();
    public String getDescription();
    public String getFilename();
}
