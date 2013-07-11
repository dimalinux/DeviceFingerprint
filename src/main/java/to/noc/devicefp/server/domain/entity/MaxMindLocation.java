/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Index;

@Entity
@Table(name="max_mind")
public class MaxMindLocation implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max=45) // 45 = max string length of IPV6 address
    @Column(name = "ip_address", length=45)
    @Index(name = "ip_stamp_idx", columnNames = {"ip_address", "stamp"})
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "stamp")
    @Index(name = "ip_stamp_idx")
    private Date stamp;

    private Double latitude;
    private Double longitude;

    @Size(max=80)
    @Column(length=80)
    private String countryCode;

    @Size(max=80)
    @Column(length=80)
    private String countryName;

    @Size(max=80)
    @Column(length=80)
    private String regionCode;

    @Size(max=80)
    @Column(length=80)
    private String regionName;

    @Size(max=80)
    @Column(length=80)
    private String city;

    @Size(max=80)
    @Column(length=80)
    private String metroCode;

    @Size(max=80)
    @Column(length=80)
    private String areaCode;

    @Size(max=80)
    @Column(length=80)
    private String timeZone;

    @Size(max=80)
    @Column(length=80)
    private String continent;

    @Size(max=80)
    @Column(length=80)
    private String postalCode;

    @Size(max=80)
    @Column(length=80)
    private String isp;

    @Size(max=80)
    @Column(length=80)
    private String org;

    @Size(max=80)
    @Column(length=80)
    private String domain;

    @Size(max=80)
    @Column(length=80)
    private String asnum;

    @Size(max=80)
    @Column(length=80)
    private String netSpeed;

    @Size(max=80)
    @Column(length=80)
    private String userType;

    private Integer accuracyRadius;

    @Size(max=80)
    @Column(length=80)
    private String countryConf;

    @Size(max=80)
    @Column(length=80)
    private String cityConf;

    @Size(max=80)
    @Column(length=80)
    private String regionConf;

    @Size(max=80)
    @Column(length=80)
    private String postalConf;

    @Size(max=80)
    @Column(length=80)
    private String error;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getStamp() {
        return stamp;
    }

    public void setStamp(Date stamp) {
        this.stamp = stamp;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMetroCode() {
        return metroCode;
    }

    public void setMetroCode(String metroCode) {
        this.metroCode = metroCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAsnum() {
        return asnum;
    }

    public void setAsnum(String asnum) {
        this.asnum = asnum;
    }

    public String getNetSpeed() {
        return netSpeed;
    }

    public void setNetSpeed(String netSpeed) {
        this.netSpeed = netSpeed;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getAccuracyRadius() {
        return accuracyRadius;
    }

    public void setAccuracyRadius(Integer accuracyRadius) {
        this.accuracyRadius = accuracyRadius;
    }

    public String getCountryConf() {
        return countryConf;
    }

    public void setCountryConf(String countryConf) {
        this.countryConf = countryConf;
    }

    public String getCityConf() {
        return cityConf;
    }

    public void setCityConf(String cityConf) {
        this.cityConf = cityConf;
    }

    public String getRegionConf() {
        return regionConf;
    }

    public void setRegionConf(String regionConf) {
        this.regionConf = regionConf;
    }

    public String getPostalConf() {
        return postalConf;
    }

    public void setPostalConf(String postalConf) {
        this.postalConf = postalConf;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // used on server side for logging
    public String getAddress() {
        StringBuilder sb = new StringBuilder();
        if (city != null) {
            sb.append(city).append(",");
        }
        if (regionName != null) {
            sb.append(regionName).append(",");
        }
        if (countryName != null) {
            sb.append(countryName);
        }
        if (error != null) {
            sb.append(error);
        }
        return sb.toString();
    }


    //
    //  Note:  This is not inside the unit tests because a user
    //  accessing the website directly from a private IP address will
    //  get this mock location.
    //
    public static MaxMindLocation createMockLocation(String ip, Date stamp) {
        MaxMindLocation location = new MaxMindLocation();
        location.setIpAddress(ip);
        location.setStamp(stamp);
        location.setCountryCode("CC");
        location.setCountryName("Mock Country");
        location.setRegionCode("RC");
        location.setRegionName("Mock Region");
        location.setCity("Mock City");
        location.setLatitude(55.748106);
        location.setLongitude(37.613647);
        location.setMetroCode("XXX");
        location.setAreaCode("000");
        location.setTimeZone("TZ");
        location.setContinent("CO");
        location.setPostalCode("00000");
        location.setIsp("Mock ISP");
        location.setOrg("Mock Org");
        location.setDomain("example.org");
        location.setAsnum("ASN Mock");
        location.setNetSpeed("Very Fast");
        location.setUserType("Mock Type");
        location.setAccuracyRadius(1000);
        location.setCountryConf("50");
        location.setCityConf("50");
        location.setRegionConf("50");
        location.setPostalConf("50");
        location.setError("No Error");
        return location;
    }

}
