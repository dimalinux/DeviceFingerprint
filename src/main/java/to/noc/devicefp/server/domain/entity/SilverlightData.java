/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="silverlight")
public class SilverlightData extends DeviceFkAsPk implements Serializable {
        
    @Size(max=80)
    @Column(length=80)
    private String osVersion;

    @Size(max=80)
    @Column(length=80)
    private String clrVersion;
    
    @Size(max=80)
    @Column(length=80)
    private String assemblyClrVersion;

    private Integer processorCount;
    private Integer sysUptimeMs;

    private Boolean isolatedStorageEnabled;
    private Boolean isolatedStorageTest;
        
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getClrVersion() {
        return clrVersion;
    }

    public void setClrVersion(String clrVersion) {
        this.clrVersion = clrVersion;
    }

    public String getAssemblyClrVersion() {
        return assemblyClrVersion;
    }

    public void setAssemblyClrVersion(String assemblyClrVersion) {
        this.assemblyClrVersion = assemblyClrVersion;
    }

    public Integer getProcessorCount() {
        return processorCount;
    }

    public void setProcessorCount(Integer processorCount) {
        this.processorCount = processorCount;
    }

    public Integer getSysUptimeMs() {
        return sysUptimeMs;
    }

    public void setSysUptimeMs(Integer sysUptimeMs) {
        this.sysUptimeMs = sysUptimeMs;
    }

    public Boolean getIsolatedStorageEnabled() {
        return isolatedStorageEnabled;
    }

    public void setIsolatedStorageEnabled(Boolean isolatedStorageEnabled) {
        this.isolatedStorageEnabled = isolatedStorageEnabled;
    }

    public Boolean getIsolatedStorageTest() {
        return isolatedStorageTest;
    }

    public void setIsolatedStorageTest(Boolean isolatedStorageTest) {
        this.isolatedStorageTest = isolatedStorageTest;
    }

}
