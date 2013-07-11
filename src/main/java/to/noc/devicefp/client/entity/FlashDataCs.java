/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

/* Client-side (Cs) interface for classes holding FlashData */
public interface FlashDataCs {

    public Boolean getAvHardwareDisable();
    public String getCpuArchitecture();
    public Boolean getHasAccessibility();
    public Boolean getHasAudio();
    public Boolean getHasAudioEncoder();
    public Boolean getHasEmbeddedVideo();
    public Boolean getHasIME();
    public Boolean getHasMP3();
    public Boolean getHasPrinting();
    public Boolean getHasScreenBroadcast();
    public Boolean getHasScreenPlayback();
    public Boolean getHasStreamingAudio();
    public Boolean getHasStreamingVideo();
    public Boolean getHasTLS();
    public Boolean getHasVideoEncoder();
    public Boolean getIsDebugger();
    public String getLanguage();
    public Boolean getLocalFileReadDisable();
    public String getManufacturer();
    public String getMaxLevelIDC();
    public String getOs();
    public Float getPixelAspectRatio();
    public String getPlayerType();
    public String getScreenColor();
    public Integer getScreenDPI();
    public Integer getScreenResolutionX();
    public Integer getScreenResolutionY();
    public Boolean getSupports32BitProcesses();
    public Boolean getSupports64BitProcesses();
    public String getTouchscreenType();
    public String getVersion();
    public Boolean getLsoStorageTest();
}
