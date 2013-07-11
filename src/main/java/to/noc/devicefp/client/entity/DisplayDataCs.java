/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

public interface DisplayDataCs {

    public Integer getWidth();
    public Integer getHeight();
    public Integer getAvailWidth();
    public Integer getAvailHeight();
    public Integer getColorDepth();
    public Integer getPixelDepth();
    public Float getPixelRatio(); 
    public Integer getInnerWidth();
    public Integer getInnerHeight();
    public Integer getOuterWidth();
    public Integer getOuterHeight();
    public Integer getPageXOffset();
    public Integer getPageYOffset();
    public Integer getClientWidth();
    public Integer getClientHeight();
    public Integer getOffsetWidth(); 
    public Integer getOffsetHeight();
    public Integer getScreenX();
    public Integer getScreenY();         
    public Boolean getFontSmoothingEnabled();
    public Integer getBufferDepth();
    public Integer getDeviceXDPI();
    public Integer getDeviceYDPI();
    public Integer getLogicalXDPI();
    public Integer getLogicalYDPI();  
    public Integer getSystemXDPI();   
    public Integer getSystemYDPI();
    public Integer getUpdateInterval();
}
