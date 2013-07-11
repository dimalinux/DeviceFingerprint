/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

/* Client-side (Cs) interface for classes holding Java data */
public interface JavaDataCs {
    public String getJavaVendor();
    public void setJavaVendor(String javaVendor);

    public String getJavaVendorUrl();
    public void setJavaVendorUrl(String javaVendorUrl);

    public String getJavaVersion();
    public void setJavaVersion(String javaVersion);

    public String getLanIpAddress();
    public void setLanIpAddress(String lanIpAddress);

    public String getOsArch();
    public void setOsArch(String osArch);

    public String getOsName();
    public void setOsName(String osName);

    public String getOsVersion();
    public void setOsVersion(String osVersion);
}
