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
@Table(name="flash")
public class FlashData extends DeviceFkAsPk implements Serializable {

    private Boolean avHardwareDisable;
    
    @Size(max=120)
    @Column(length=120)
    private String cpuArchitecture;
    
    private Boolean hasAccessibility;
    private Boolean hasAudio;
    private Boolean hasAudioEncoder;
    private Boolean hasEmbeddedVideo;
    @Column(name="has_ime")
    private Boolean hasIME;
    @Column(name="has_mp3")
    private Boolean hasMP3;
    private Boolean hasPrinting;
    private Boolean hasScreenBroadcast;
    private Boolean hasScreenPlayback;
    private Boolean hasStreamingAudio;
    private Boolean hasStreamingVideo;
    @Column(name="has_tls")
    private Boolean hasTLS;
    private Boolean hasVideoEncoder;
    private Boolean isDebugger;
    //private Boolean isEmbeddedInAcrobat;
    private Boolean lsoStorageTest;
    
    @Size(max=80)
    @Column(length=80, name = "lang")
    private String language;
    
    private Boolean localFileReadDisable;
    
    @Size(max=80)
    @Column(length=80)
    private String manufacturer;
    
    @Size(max=80)
    @Column(name="max_level_idc", length=80)
    private String maxLevelIDC;
    
    @Size(max=80)
    @Column(length=80)
    private String os;
    
    private Float pixelAspectRatio;
    
    @Size(max=60)
    @Column(length=60)
    private String playerType;
    
    @Size(max=60)
    @Column(length=60)
    private String screenColor;
    
    @Column(name="screen_dpi")
    private Integer screenDPI;
    @Column(name="screen_res_x")
    private Integer screenResolutionX;
    @Column(name="screen_res_y")
    private Integer screenResolutionY;
    private Boolean supports32BitProcesses;
    private Boolean supports64BitProcesses;
    
    @Size(max=80)
    @Column(length=80)
    private String touchscreenType;    
    
    @Size(max=120)
    @Column(length=120, name = "flash_version")
    private String version;

    public Boolean getAvHardwareDisable() {
        return avHardwareDisable;
    }

    public void setAvHardwareDisable(Boolean avHardwareDisable) {
        this.avHardwareDisable = avHardwareDisable;
    }

    public String getCpuArchitecture() {
        return cpuArchitecture;
    }

    public void setCpuArchitecture(String cpuArchitecture) {
        this.cpuArchitecture = cpuArchitecture;
    }

    public Boolean getHasAccessibility() {
        return hasAccessibility;
    }

    public void setHasAccessibility(Boolean hasAccessibility) {
        this.hasAccessibility = hasAccessibility;
    }

    public Boolean getHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(Boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public Boolean getHasAudioEncoder() {
        return hasAudioEncoder;
    }

    public void setHasAudioEncoder(Boolean hasAudioEncoder) {
        this.hasAudioEncoder = hasAudioEncoder;
    }

    public Boolean getHasEmbeddedVideo() {
        return hasEmbeddedVideo;
    }

    public void setHasEmbeddedVideo(Boolean hasEmbeddedVideo) {
        this.hasEmbeddedVideo = hasEmbeddedVideo;
    }

    public Boolean getHasIME() {
        return hasIME;
    }

    public void setHasIME(Boolean hasIME) {
        this.hasIME = hasIME;
    }

    public Boolean getHasMP3() {
        return hasMP3;
    }

    public void setHasMP3(Boolean hasMP3) {
        this.hasMP3 = hasMP3;
    }

    public Boolean getHasPrinting() {
        return hasPrinting;
    }

    public void setHasPrinting(Boolean hasPrinting) {
        this.hasPrinting = hasPrinting;
    }

    public Boolean getHasScreenBroadcast() {
        return hasScreenBroadcast;
    }

    public void setHasScreenBroadcast(Boolean hasScreenBroadcast) {
        this.hasScreenBroadcast = hasScreenBroadcast;
    }

    public Boolean getHasScreenPlayback() {
        return hasScreenPlayback;
    }

    public void setHasScreenPlayback(Boolean hasScreenPlayback) {
        this.hasScreenPlayback = hasScreenPlayback;
    }

    public Boolean getHasStreamingAudio() {
        return hasStreamingAudio;
    }

    public void setHasStreamingAudio(Boolean hasStreamingAudio) {
        this.hasStreamingAudio = hasStreamingAudio;
    }

    public Boolean getHasStreamingVideo() {
        return hasStreamingVideo;
    }

    public void setHasStreamingVideo(Boolean hasStreamingVideo) {
        this.hasStreamingVideo = hasStreamingVideo;
    }

    public Boolean getHasTLS() {
        return hasTLS;
    }

    public void setHasTLS(Boolean hasTLS) {
        this.hasTLS = hasTLS;
    }

    public Boolean getHasVideoEncoder() {
        return hasVideoEncoder;
    }

    public void setHasVideoEncoder(Boolean hasVideoEncoder) {
        this.hasVideoEncoder = hasVideoEncoder;
    }

    public Boolean getIsDebugger() {
        return isDebugger;
    }

    public void setIsDebugger(Boolean isDebugger) {
        this.isDebugger = isDebugger;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getLocalFileReadDisable() {
        return localFileReadDisable;
    }

    public void setLocalFileReadDisable(Boolean localFileReadDisable) {
        this.localFileReadDisable = localFileReadDisable;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMaxLevelIDC() {
        return maxLevelIDC;
    }

    public void setMaxLevelIDC(String maxLevelIDC) {
        this.maxLevelIDC = maxLevelIDC;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Float getPixelAspectRatio() {
        return pixelAspectRatio;
    }

    public void setPixelAspectRatio(Float pixelAspectRatio) {
        this.pixelAspectRatio = pixelAspectRatio;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getScreenColor() {
        return screenColor;
    }

    public void setScreenColor(String screenColor) {
        this.screenColor = screenColor;
    }

    public Integer getScreenDPI() {
        return screenDPI;
    }

    public void setScreenDPI(Integer screenDPI) {
        this.screenDPI = screenDPI;
    }

    public Integer getScreenResolutionX() {
        return screenResolutionX;
    }

    public void setScreenResolutionX(Integer screenResolutionX) {
        this.screenResolutionX = screenResolutionX;
    }

    public Integer getScreenResolutionY() {
        return screenResolutionY;
    }

    public void setScreenResolutionY(Integer screenResolutionY) {
        this.screenResolutionY = screenResolutionY;
    }

    public Boolean getSupports32BitProcesses() {
        return supports32BitProcesses;
    }

    public void setSupports32BitProcesses(Boolean supports32BitProcesses) {
        this.supports32BitProcesses = supports32BitProcesses;
    }

    public Boolean getSupports64BitProcesses() {
        return supports64BitProcesses;
    }

    public void setSupports64BitProcesses(Boolean supports64BitProcesses) {
        this.supports64BitProcesses = supports64BitProcesses;
    }

    public String getTouchscreenType() {
        return touchscreenType;
    }

    public void setTouchscreenType(String touchscreenType) {
        this.touchscreenType = touchscreenType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getLsoStorageTest() {
        return lsoStorageTest;
    }

    public void setLsoStorageTest(Boolean lsoStorageTest) {
        this.lsoStorageTest = lsoStorageTest;
    }
}
