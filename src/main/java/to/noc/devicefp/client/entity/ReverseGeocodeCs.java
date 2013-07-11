/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

public interface ReverseGeocodeCs {
    
    public String getStatus();

    public Double getLatitude();

    public Double getLongitude();

    public String getAddress();

    public String getAddressType();

    public String getStreetNumber();

    public String getRoute();

    public String getNeighborhood();

    public String getLocality();

    public String getAdministrativeAreaLevel1();

    public String getAdministrativeAreaLevel2();

    public String getCountry();

    public String getPostalCode();
}
