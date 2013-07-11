/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import java.util.Date;

public interface MaxMindLocationCs {

    public String getIpAddress();
    public Date getStamp();
    public Double getLatitude();
    public Double getLongitude();
    public Integer getAccuracyRadius();
    public String getCountryCode();
    public String getCountryName();
    public String getRegionCode();
    public String getRegionName();
    public String getCity();
    public String getMetroCode();
    public String getAreaCode();
    public String getTimeZone();
    public String getContinent();
    public String getPostalCode();
    public String getIsp();
    public String getOrg();
    public String getDomain();
    public String getAsnum();
    public String getNetSpeed();
    public String getUserType();   
    public String getCountryConf();
    public String getCityConf();
    public String getRegionConf();
    public String getPostalConf();
    public String getError();
}


