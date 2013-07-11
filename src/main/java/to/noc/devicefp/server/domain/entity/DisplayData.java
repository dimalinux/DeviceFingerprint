/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="display")
public class DisplayData extends DeviceFkAsPk implements Serializable {
       
    private Integer width;
    private Integer height;
    private Integer availWidth;
    private Integer availHeight;
    private Integer colorDepth;
    private Integer pixelDepth;
    private Float   pixelRatio;

    private Integer innerWidth;
    private Integer innerHeight;
    private Integer outerWidth;
    private Integer outerHeight;
    
    @Column(name="page_x_offset")
    private Integer pageXOffset;
    @Column(name="page_y_offset")
    private Integer pageYOffset;
    
    private Integer clientWidth;
    private Integer clientHeight;
    
    private Integer offsetWidth;
    private Integer offsetHeight;
    
    @Column(name="screen_x")
    private Integer screenX;
    @Column(name="screen_y")
    private Integer screenY;
    
    /* Remaining are IE specific values */
    private Boolean fontSmoothingEnabled;
    private Integer bufferDepth;
    
    @Column(name="device_x_dpi")
    private Integer deviceXDPI;    
    @Column(name="device_y_dpi")
    private Integer deviceYDPI;
    
    @Column(name="logical_x_dpi")
    private Integer logicalXDPI;   
    @Column(name="logical_y_dpi")
    private Integer logicalYDPI;
    
    @Column(name="system_x_dpi")
    private Integer systemXDPI;     
    @Column(name="system_y_dpi")
    private Integer systemYDPI;
    
    private Integer updateInterval;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getAvailWidth() {
        return availWidth;
    }

    public void setAvailWidth(Integer availWidth) {
        this.availWidth = availWidth;
    }

    public Integer getAvailHeight() {
        return availHeight;
    }

    public void setAvailHeight(Integer availHeight) {
        this.availHeight = availHeight;
    }

    public Integer getColorDepth() {
        return colorDepth;
    }

    public void setColorDepth(Integer colorDepth) {
        this.colorDepth = colorDepth;
    }

    public Integer getPixelDepth() {
        return pixelDepth;
    }

    public void setPixelDepth(Integer pixelDepth) {
        this.pixelDepth = pixelDepth;
    }

    public Float getPixelRatio() {
        return pixelRatio;
    }

    public void setPixelRatio(Float pixelRatio) {
        this.pixelRatio = pixelRatio;
    }

    public Integer getInnerWidth() {
        return innerWidth;
    }

    public void setInnerWidth(Integer innerWidth) {
        this.innerWidth = innerWidth;
    }

    public Integer getInnerHeight() {
        return innerHeight;
    }

    public void setInnerHeight(Integer innerHeight) {
        this.innerHeight = innerHeight;
    }

    public Integer getOuterWidth() {
        return outerWidth;
    }

    public void setOuterWidth(Integer outerWidth) {
        this.outerWidth = outerWidth;
    }

    public Integer getOuterHeight() {
        return outerHeight;
    }

    public void setOuterHeight(Integer outerHeight) {
        this.outerHeight = outerHeight;
    }

    public Integer getPageXOffset() {
        return pageXOffset;
    }

    public void setPageXOffset(Integer pageXOffset) {
        this.pageXOffset = pageXOffset;
    }

    public Integer getPageYOffset() {
        return pageYOffset;
    }

    public void setPageYOffset(Integer pageYOffset) {
        this.pageYOffset = pageYOffset;
    }

    public Integer getClientWidth() {
        return clientWidth;
    }

    public void setClientWidth(Integer clientWidth) {
        this.clientWidth = clientWidth;
    }

    public Integer getClientHeight() {
        return clientHeight;
    }

    public void setClientHeight(Integer clientHeight) {
        this.clientHeight = clientHeight;
    }

    public Integer getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetWidth(Integer offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public Integer getOffsetHeight() {
        return offsetHeight;
    }

    public void setOffsetHeight(Integer offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public Integer getScreenX() {
        return screenX;
    }

    public void setScreenX(Integer screenX) {
        this.screenX = screenX;
    }

    public Integer getScreenY() {
        return screenY;
    }

    public void setScreenY(Integer screenY) {
        this.screenY = screenY;
    }

    public Boolean getFontSmoothingEnabled() {
        return fontSmoothingEnabled;
    }

    public void setFontSmoothingEnabled(Boolean fontSmoothingEnabled) {
        this.fontSmoothingEnabled = fontSmoothingEnabled;
    }

    public Integer getBufferDepth() {
        return bufferDepth;
    }

    public void setBufferDepth(Integer bufferDepth) {
        this.bufferDepth = bufferDepth;
    }

    public Integer getDeviceXDPI() {
        return deviceXDPI;
    }

    public void setDeviceXDPI(Integer deviceXDPI) {
        this.deviceXDPI = deviceXDPI;
    }

    public Integer getDeviceYDPI() {
        return deviceYDPI;
    }

    public void setDeviceYDPI(Integer deviceYDPI) {
        this.deviceYDPI = deviceYDPI;
    }

    public Integer getLogicalXDPI() {
        return logicalXDPI;
    }

    public void setLogicalXDPI(Integer logicalXDPI) {
        this.logicalXDPI = logicalXDPI;
    }

    public Integer getLogicalYDPI() {
        return logicalYDPI;
    }

    public void setLogicalYDPI(Integer logicalYDPI) {
        this.logicalYDPI = logicalYDPI;
    }

    public Integer getSystemXDPI() {
        return systemXDPI;
    }

    public void setSystemXDPI(Integer systemXDPI) {
        this.systemXDPI = systemXDPI;
    }

    public Integer getSystemYDPI() {
        return systemYDPI;
    }

    public void setSystemYDPI(Integer systemYDPI) {
        this.systemYDPI = systemYDPI;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

}
