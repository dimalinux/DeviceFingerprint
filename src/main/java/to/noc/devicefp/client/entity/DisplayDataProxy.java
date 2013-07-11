/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.DisplayData;

@ProxyFor(value = DisplayData.class)
public interface DisplayDataProxy extends DisplayDataCs, ValueProxy {
    public void setWidth(Integer width);
    public void setHeight(Integer height);
    public void setAvailWidth(Integer availWidth);
    public void setAvailHeight(Integer availHeight);
    public void setColorDepth(Integer colorDepth);
    public void setPixelDepth(Integer pixelDepth);
    public void setPixelRatio(Float pixelRatio);
    public void setInnerWidth(Integer innerWidth);
    public void setInnerHeight(Integer innerHeight);
    public void setOuterWidth(Integer outerWidth);
    public void setOuterHeight(Integer outerHeight);
    public void setPageXOffset(Integer pageXOffset);
    public void setPageYOffset(Integer pageYOffset);
    public void setClientWidth(Integer clientWidth);
    public void setClientHeight(Integer clientHeight);
    public void setOffsetWidth(Integer offsetWidth);
    public void setOffsetHeight(Integer offsetHeight);    
    public void setScreenX(Integer screenX);
    public void setScreenY(Integer screenY);
    public void setFontSmoothingEnabled(Boolean fontSmoothingEnabled);
    public void setBufferDepth(Integer bufferDepth);
    public void setDeviceXDPI(Integer deviceXDPI);
    public void setDeviceYDPI(Integer deviceYDPI);
    public void setLogicalXDPI(Integer logicalXDPI);
    public void setLogicalYDPI(Integer logicalYDPI);
    public void setSystemXDPI(Integer systemXDPI);
    public void setSystemYDPI(Integer systemYDPI);
    public void setUpdateInterval(Integer updateInterval);
}
