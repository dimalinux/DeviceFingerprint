/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="js")
public class JsData extends DeviceFkAsPk implements Serializable {

    private Boolean cookiesEnabled;
    private Boolean cookiesTest;
    private Boolean javaEnabled;       
    private Boolean geolocationCapable;
    
    @Column(name="html5storage_capable") // todo: rename column and remove this
    private Boolean webLocalStorageCapable;
    private Boolean webLocalStorageTest;

    @Size(max=40)
    @Column(length=40)
    private String product;
    
    @Size(max=40)
    @Column(length=40)
    private String productSub;
    
    @Size(max=250)
    @Column(length=250)
    private String appCodeName;
    
    @Size(max=250)
    @Column(length=250)
    private String appName;
    
    @Size(max=1020)
    @Column(length=1020)
    private String appVersion;
    
    @Size(max=120)
    @Column(length=120)
    private String appMinorVersion;
    
    @Size(max=120)
    @Column(length=120)
    private String platform;
    
    @Size(max=1020)
    @Column(length=1020)
    private String userAgent;
    
    @Size(max=120)
    @Column(length=120)
    private String vendor;
    
    @Size(max=120)
    @Column(length=120)
    private String vendorSub;
    
    @Size(max=40)
    @Column(name="lang", length=40)
    private String language;
    
    @Size(max=40)
    @Column(name="user_lang", length=40)
    private String userLanguage;
    
    @Size(max=40)
    @Column(name="browser_lang", length=40)
    private String browserLanguage;
    
    @Size(max=40)
    @Column(name="system_lang", length=40)
    private String systemLanguage;
    
    @Size(max=40)
    @Column(length=40)
    private String cpuClass;
    
    @Size(max=40)
    @Column(length=40)
    private String oscpu;

    @Size(max=40)
    @Column(name="build_id", length=40)
    private String buildID;
    
    @Size(max=40)
    @Column(length=40)
    private String doNotTrack;
    
    private Boolean msDoNotTrack;
           
    @Temporal(TemporalType.TIMESTAMP)
    private Date clientStamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date windowCloseStamp;    
    
    @Column(name="gmt_offset_min", length=40)
    private Integer utcOffsetMin;
  
    
    public Boolean getCookiesEnabled() {
        return cookiesEnabled;
    }

    public void setCookiesEnabled(Boolean cookiesEnabled) {
        this.cookiesEnabled = cookiesEnabled;
    }
      
    public Boolean getCookiesTest() {
        return cookiesTest;
    }

    public void setCookiesTest(Boolean cookiesTest) {
        this.cookiesTest = cookiesTest;
    }

    public Boolean getJavaEnabled() {
        return javaEnabled;
    }

    public void setJavaEnabled(Boolean javaEnabled) {
        this.javaEnabled = javaEnabled;
    }

    public Boolean getGeolocationCapable() {
        return geolocationCapable;
    }

    public void setGeolocationCapable(Boolean geolocationCapable) {
        this.geolocationCapable = geolocationCapable;
    }

    public Boolean getWebLocalStorageCapable() {
        return webLocalStorageCapable;
    }

    public void setWebLocalStorageCapable(Boolean webLocalStorageCapable) {
        this.webLocalStorageCapable = webLocalStorageCapable;
    }

    public Boolean getWebLocalStorageTest() {
        return webLocalStorageTest;
    }

    public void setWebLocalStorageTest(Boolean webLocalStorageTest) {
        this.webLocalStorageTest = webLocalStorageTest;
    }
        
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductSub() {
        return productSub;
    }

    public void setProductSub(String productSub) {
        this.productSub = productSub;
    }

    public String getAppCodeName() {
        return appCodeName;
    }

    public void setAppCodeName(String appCodeName) {
        this.appCodeName = appCodeName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppMinorVersion() {
        return appMinorVersion;
    }

    public void setAppMinorVersion(String appMinorVersion) {
        this.appMinorVersion = appMinorVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendorSub() {
        return vendorSub;
    }

    public void setVendorSub(String vendorSub) {
        this.vendorSub = vendorSub;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public String getBrowserLanguage() {
        return browserLanguage;
    }

    public void setBrowserLanguage(String browserLanguage) {
        this.browserLanguage = browserLanguage;
    }

    public String getSystemLanguage() {
        return systemLanguage;
    }

    public void setSystemLanguage(String systemLanguage) {
        this.systemLanguage = systemLanguage;
    }

    public String getCpuClass() {
        return cpuClass;
    }

    public void setCpuClass(String cpuClass) {
        this.cpuClass = cpuClass;
    }

    public String getOscpu() {
        return oscpu;
    }

    public void setOscpu(String oscpu) {
        this.oscpu = oscpu;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getDoNotTrack() {
        return doNotTrack;
    }

    public void setDoNotTrack(String doNotTrack) {
        this.doNotTrack = doNotTrack;
    }

    public Boolean getMsDoNotTrack() {
        return msDoNotTrack;
    }

    public void setMsDoNotTrack(Boolean msDoNotTrack) {
        this.msDoNotTrack = msDoNotTrack;
    }

    public Date getClientStamp() {
        return clientStamp;
    }

    public void setClientStamp(Date clientStamp) {
        this.clientStamp = clientStamp;
    }

     public Date getWindowCloseStamp() {
        return windowCloseStamp;
    }

    public void setWindowCloseStamp(Date windowCloseStamp) {
        this.windowCloseStamp = windowCloseStamp;
    }        
    
    public Integer getUtcOffsetMin() {
        return utcOffsetMin;
    }

    public void setUtcOffsetMin(Integer utcOffsetMin) {
        this.utcOffsetMin = utcOffsetMin;
    }
}
