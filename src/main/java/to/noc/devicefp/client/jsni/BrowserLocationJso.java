/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import java.util.Date;
import to.noc.devicefp.client.entity.BrowserLocationCs;

/**
 *   JavaScriptObject which can wrap both Position and PositionError objects from the Geolocation API.
 *   This seemed simplest, since we want to coerce both objects to the same type when they are stored
 *   on the server.
 */
public final class BrowserLocationJso extends JavaScriptObject implements BrowserLocationCs {

    protected BrowserLocationJso() {
    }

    // IOS 6.0 had a bug and returned microseconds instead of milliseconds. 6.1 fixed
    // the issue.
    private native Double getStampMs() /*-{
        var value = this.timestamp;
        if (value && value > 14000000000000) {
            value /= 1000;
        }
        return (typeof value === 'number') ? @java.lang.Double::valueOf(D)(value) : null;
    }-*/;

    @Override
    public Date getStamp() {
        Double stampMs = getStampMs();
        return (stampMs != null) ? new Date(Math.round(stampMs)) : null;
    }
    
    @Override
    public native Double getLatitude() /*-{
        var value = this.coords ? this.coords.latitude : null;
        return (typeof value === 'number') ? @java.lang.Double::valueOf(D)(value) : null;
    }-*/;

    @Override
    public native Double getLongitude() /*-{
        var value = this.coords ? this.coords.longitude : null;
        return (typeof value === 'number') ? @java.lang.Double::valueOf(D)(value) : null;
    }-*/;

    @Override
    public native Integer getAccuracyRadius() /*-{
        var value = (this.coords) ? Math.round(this.coords.accuracy) : null;
        return (typeof value === 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
     }-*/;

    // Desktop Firefox returns 0 for both altitude and altitudeAccuracy.  If altitudeAccuracy is null or zero,
    // we want to return null for the altitude.  Altitude can legitimately be zero if and only if
    // altitudeAccuracy is non-zero.
    @Override
    public native Double getAltitude() /*-{
        var value = (this.coords && this.coords.altitudeAccuracy) ? this.coords.altitude : null;
        return (typeof value === 'number') ? @java.lang.Double::valueOf(D)(value) : null;
     }-*/;

    // Note: The code below will correctly return null if altitudeAccuracy is zero
    @Override
    public native Integer getAltitudeAccuracy() /*-{
        var value = (this.coords && this.coords.altitudeAccuracy) ? Math.round(this.coords.altitudeAccuracy) : null;
        return (typeof value === 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
     }-*/;

    // Desktop Firefox returns NaN (instead of null) for heading and speed. NaN evaluates to false, so
    // the code below will correctly return null in this scenario.
    @Override
    public native Double getHeading() /*-{
        var value = (this.coords && this.coords.heading) ? this.coords.heading : null;
        return (typeof value === 'number') ? @java.lang.Double::valueOf(D)(value) : null;
     }-*/;

    @Override
    public native Double getSpeed() /*-{
        var value = (this.coords && this.coords.speed) ? this.coords.speed : null;
        return (typeof value === 'number') ? @java.lang.Double::valueOf(D)(value) : null;
    }-*/;
    
    @Override
    public native Integer getErrorCode() /*-{
        var value = this.code;
        return (typeof value === 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;
    
    @Override
    public native String getErrorMessage() /*-{
        return this.message || null;
    }-*/;
}
