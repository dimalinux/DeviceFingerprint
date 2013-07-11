/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import java.util.Date;
import to.noc.devicefp.client.entity.MaxMindLocationCs;

public final class MaxMindLocationJso extends JavaScriptObject implements MaxMindLocationCs {

    protected MaxMindLocationJso() {}

    @Override
    public native String getIpAddress() /*-{
        return this.ipAddress;
    }-*/;
    
    private native double getStampMs() /*-{
        return this.stamp;
    }-*/;
    
    @Override
    public Date getStamp() {
        return new Date((long)getStampMs());
    }    

    @Override
    public native Double getLatitude() /*-{
        var value = this.latitude;
        return (typeof value == 'number') ? @java.lang.Double::valueOf(D)(value) : null;
    }-*/;
    
    @Override
    public native Double getLongitude() /*-{
        var value = this.longitude;
        return (typeof value == 'number') ? @java.lang.Double::valueOf(D)(value) : null;
    }-*/;

    @Override
    public native Integer getAccuracyRadius() /*-{
        var value = this.accuracyRadius;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;
    
    @Override
    public native String getCountryCode() /*-{
        return this.countryCode;
    }-*/;

    @Override
    public native String getCountryName() /*-{
        return this.countryName;
    }-*/;

    @Override
    public native String getRegionCode() /*-{
        return this.regionCode;
    }-*/;

    @Override
    public native String getRegionName() /*-{
        return this.regionName;
    }-*/;

    @Override
    public native String getCity() /*-{
        return this.city;
    }-*/;

    @Override
    public native String getMetroCode() /*-{
        return this.metroCode;
    }-*/;

    @Override
    public native String getAreaCode() /*-{
        return this.areaCode;
    }-*/;

    @Override
    public native String getTimeZone() /*-{
        return this.timeZone;
    }-*/;

    @Override
    public native String getContinent() /*-{
        return this.continent;
    }-*/;

    @Override
    public native String getPostalCode() /*-{
        return this.postalCode;
    }-*/;

    @Override
    public native String getIsp() /*-{
        return this.isp;
    }-*/;

    @Override
    public native String getOrg() /*-{
        return this.org;
    }-*/;

    @Override
    public native String getDomain() /*-{
        return this.domain;
    }-*/;

    @Override
    public native String getAsnum() /*-{
        return this.asnum;
    }-*/;

    @Override
    public native String getNetSpeed() /*-{
        return this.netSpeed;
    }-*/;

    @Override
    public native String getUserType() /*-{
        return this.userType;
    }-*/;

    @Override
    public native String getCountryConf() /*-{
        return this.countryConf;
    }-*/;

    @Override
    public native String getCityConf() /*-{
        return this.cityConf;
    }-*/;

    @Override
    public native String getRegionConf() /*-{
        return this.regionConf;
    }-*/;

    @Override
    public native String getPostalConf() /*-{
        return this.postalConf;
    }-*/;

    @Override
    public native String getError() /*-{
        return this.error;
    }-*/;

}