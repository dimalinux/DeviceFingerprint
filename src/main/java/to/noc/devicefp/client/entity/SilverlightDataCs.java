/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

/* Client-side (Cs) interface for classes holding SilverlightData */
public interface SilverlightDataCs {   
    public String getOsVersion();
    public String getClrVersion();
    public String getAssemblyClrVersion();
    public Integer getProcessorCount();
    public Integer getSysUptimeMs();    
    public Boolean getIsolatedStorageEnabled();
    public Boolean getIsolatedStorageTest();
}
