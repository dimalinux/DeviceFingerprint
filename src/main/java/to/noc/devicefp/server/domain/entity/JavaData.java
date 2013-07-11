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
@Table(name="java")
public class JavaData extends DeviceFkAsPk implements Serializable {
    
    @Size(max=80)
    @Column(length=80)
    private String javaVendor;
    
    @Size(max=250)
    @Column(length=250)
    private String javaVendorUrl;

    @Size(max=80)
    @Column(length=80)
    private String javaVersion;
    
    @Size(max=80)
    @Column(length=80)
    private String osArch;
    
    @Size(max=80)
    @Column(length=80)
    private String osName;
    
    @Size(max=80)
    @Column(length=80)
    private String osVersion;
    
    @Size(max=500) /* Extra length in case it contains an error message */
    @Column(length=500)
    private String lanIpAddress;
    
    public String getJavaVendor() {
        return javaVendor;
    }

    public void setJavaVendor(String javaVendor) {
        this.javaVendor = javaVendor;
    }

    public String getJavaVendorUrl() {
        return javaVendorUrl;
    }

    public void setJavaVendorUrl(String javaVendorUrl) {
        this.javaVendorUrl = javaVendorUrl;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getLanIpAddress() {
        return lanIpAddress;
    }

    public void setLanIpAddress(String lanIpAddress) {
        this.lanIpAddress = lanIpAddress;
    }
}
