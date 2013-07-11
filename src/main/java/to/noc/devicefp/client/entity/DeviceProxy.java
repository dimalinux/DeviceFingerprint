/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import java.util.List;
import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.service.locator.SavedDeviceLocator;

@ProxyFor(value = Device.class, locator = SavedDeviceLocator.class)
public interface DeviceProxy extends DeviceCs, EntityProxy {

    @Override
    public EntityProxyId<DeviceProxy> stableId();
    
    @Override
    public List<RequestHeaderProxy> getRequestHeaders();
    
    @Override
    public UserAgentDataProxy getUserAgentData();

    @Override
    public List<PluginProxy> getPlugins();   
    
    @Override
    public JsDataProxy getJsData();

    @Override
    public DisplayDataProxy getDisplayData();

    @Override
    public JavaDataProxy getJavaData();

    @Override
    public FlashDataProxy getFlashData();
        
    @Override
    public SilverlightDataProxy getSilverlightData();
        
    @Override
    public BrowserLocationProxy getBrowserLocation();
    
    @Override
    public ReverseGeocodeProxy getReverseGeocode();
     
    @Override
    public MaxMindLocationProxy getMaxMindLocation();
            
    @Override
    public CookieStatesProxy getCookieStates();
    
    @Override
    public TcpSynDataProxy getTcpSynData();

    @Override
    public DnsDataProxy getDnsData();

}
