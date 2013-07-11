/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import java.util.Date;

/* Client-side (Cs) interface for classes holding JavaScript data */
public interface JsDataCs {

    public Boolean getCookiesEnabled();

    public Boolean getCookiesTest();

    public Boolean getJavaEnabled();
        
    public Boolean getGeolocationCapable();
    
    public Boolean getWebLocalStorageCapable();
 
    public Boolean getWebLocalStorageTest();
               
    public String getProduct();

    public String getProductSub();
    
    public String getAppCodeName();

    public String getAppName();

    public String getAppVersion();

    public String getAppMinorVersion();

    public String getPlatform();

    public String getUserAgent();
 
    public String getVendor();

    public String getVendorSub();

    public String getLanguage();

    public String getUserLanguage();
 
    public String getBrowserLanguage();
 
    public String getSystemLanguage();
    
    public String getCpuClass();
 
    public String getOscpu();

    public String getBuildID();
 
    public String getDoNotTrack();
 
    public Boolean getMsDoNotTrack();
    
    public Date getClientStamp();
    
    public Integer getUtcOffsetMin();
    
    public Date getWindowCloseStamp();

}
