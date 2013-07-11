/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import java.util.Date;
import to.noc.devicefp.server.domain.entity.BrowserLocation;

@ProxyFor(value = BrowserLocation.class)
public interface BrowserLocationProxy extends ValueProxy, BrowserLocationCs {
    public void setStamp(Date stamp);
    public void setAccuracyRadius(Integer accuracy);
    public void setAltitude(Double altitude);
    public void setHeading(Double heading);
    public void setLatitude(Double latitude);
    public void setSpeed(Double speed);
    public void setLongitude(Double longitude);
    public void setAltitudeAccuracy(Integer altitudeAccuracy);
    public void setErrorCode(Integer errorCode);
    public void setErrorMessage(String errorMessage);
}
