/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import java.util.Date;
import to.noc.devicefp.server.domain.entity.JsData;

@ProxyFor(value = JsData.class)
public interface JsDataProxy extends ValueProxy, JsDataCs {

    public void setJavaEnabled(Boolean javaEnabled);
    
    public void setCookiesEnabled(Boolean cookiesEnabled);
    
    public void setCookiesTest(Boolean cookiesTest);
    
    public void setWebLocalStorageCapable(Boolean webLocalStorageCapable);
    
    public void setWebLocalStorageTest(Boolean webLocalStorageTest);
    
    public void setGeolocationCapable(Boolean geolocationCapable);

    public void setProduct(String product);

    public void setProductSub(String productSub);

    public void setAppCodeName(String appCodeName);

    public void setAppName(String appName);

    public void setAppVersion(String appVersion);

    public void setAppMinorVersion(String appMinorVersion);

    public void setPlatform(String platform);

    public void setUserAgent(String userAgent);

    public void setVendor(String vendor);

    public void setVendorSub(String vendorSub);

    public void setLanguage(String language);

    public void setUserLanguage(String userLanguage);

    public void setBrowserLanguage(String browserLanguage);

    public void setSystemLanguage(String systemLanguage);

    public void setCpuClass(String cpuClass);

    public void setOscpu(String oscpu);

    public void setBuildID(String buildID);

    public void setDoNotTrack(String doNotTrack);

    public void setMsDoNotTrack(Boolean msDoNotTrack);

    public void setClientStamp(Date clientStamp);

    public void setUtcOffsetMin(Integer utcOffsetMin);
}
