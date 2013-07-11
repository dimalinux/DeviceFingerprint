/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.ReverseGeocode;

@ProxyFor(value = ReverseGeocode.class)
public interface ReverseGeocodeProxy extends ValueProxy, ReverseGeocodeCs {

    public void setStatus(String status);

    public void setLatitude(Double latitude);

    public void setLongitude(Double longitude);

    public void setAddress(String address);

    public void setAddressType(String addressType);

    public void setStreetNumber(String streetNumber);

    public void setRoute(String route);

    public void setNeighborhood(String neighborhood);

    public void setLocality(String locality);

    public void setAdministrativeAreaLevel1(String administrativeAreaLevel1);

    public void setAdministrativeAreaLevel2(String administrativeAreaLevel2);

    public void setCountry(String country);

    public void setPostalCode(String postalCode);
}
