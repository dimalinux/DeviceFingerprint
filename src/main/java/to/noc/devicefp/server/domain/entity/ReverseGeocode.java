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
@Table(name = "reverse_geocode")
public class ReverseGeocode extends DeviceFkAsPk implements Serializable {
    private Double latitude;
    private Double longitude;
    
    @Size(max = 20)
    @Column(length = 20)
    private String status;
    
    @Size(max = 80)
    @Column(length = 80)
    private String address;
    
    @Size(max = 20)
    @Column(length = 20)
    private String addressType;
    
    @Size(max = 20)
    @Column(length = 20)
    private String streetNumber;
    
    @Size(max = 50)
    @Column(length = 50)
    private String route;
    
    @Size(max = 50)
    @Column(length = 50)
    private String neighborhood;
    
    @Size(max = 50)
    @Column(length = 50)
    private String locality;
    
    @Size(max = 50)
    @Column(length = 50)
    private String administrativeAreaLevel1;
    
    @Size(max = 50)
    @Column(length = 50)
    private String administrativeAreaLevel2;
    
    @Size(max = 40)
    @Column(length = 40)
    private String country;
    
    @Size(max = 20)
    @Column(length = 20)
    private String postalCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAdministrativeAreaLevel1() {
        return administrativeAreaLevel1;
    }

    public void setAdministrativeAreaLevel1(String administrativeAreaLevel1) {
        this.administrativeAreaLevel1 = administrativeAreaLevel1;
    }

    public String getAdministrativeAreaLevel2() {
        return administrativeAreaLevel2;
    }

    public void setAdministrativeAreaLevel2(String administrativeAreaLevel2) {
        this.administrativeAreaLevel2 = administrativeAreaLevel2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
