/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.ReverseGeocodeCs;

public final class ReverseGeocodeJso extends JavaScriptObject implements ReverseGeocodeCs {

    protected ReverseGeocodeJso() {}
    
    @Override
    public native String getStatus() /*-{
        return this.status;
    }-*/;
    
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
    public native String getAddress() /*-{
        return this.address;
    }-*/;
    
    @Override
    public native String getAddressType() /*-{
        return this.addressType;
    }-*/;
    
    @Override
    public native String getStreetNumber() /*-{
        return this.streetNumber;
    }-*/;
    
    @Override
    public native String getRoute() /*-{
        return this.route;
    }-*/;
    
    @Override
    public native String getNeighborhood() /*-{
        return this.neighborhood;
    }-*/;
    
    @Override
    public native String getLocality() /*-{
        return this.locality;
    }-*/;

    @Override
    public native String getAdministrativeAreaLevel1() /*-{
        return this.administrativeAreaLevel1;
    }-*/;
    
    @Override
    public native String getAdministrativeAreaLevel2() /*-{
        return this.administrativeAreaLevel2;
    }-*/;
    
    @Override
    public native String getCountry() /*-{
        return this.country;
    }-*/;
    
    @Override
    public native String getPostalCode() /*-{
        return this.postalCode;
    }-*/;

}


