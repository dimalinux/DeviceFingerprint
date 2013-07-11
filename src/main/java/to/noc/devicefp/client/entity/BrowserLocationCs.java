/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import java.util.Date;

/* Client-side (Cs) interface for classes holding Geolocation API obtained Location data */
public interface BrowserLocationCs {
    public Date getStamp();
    public Integer getAccuracyRadius();
    public Double getAltitude();
    public Double getHeading();
    public Double getLatitude();
    public Double getSpeed();
    public Double getLongitude();
    public Integer getAltitudeAccuracy();
    public Integer getErrorCode();
    public String getErrorMessage();
}
