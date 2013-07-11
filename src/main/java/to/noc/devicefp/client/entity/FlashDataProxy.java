/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.FlashData;

@ProxyFor(value = FlashData.class)
public interface FlashDataProxy extends ValueProxy, FlashDataCs {

    public void setAvHardwareDisable(Boolean avHardwareDisable);
    public void setCpuArchitecture(String cpuArchitecture);
    public void setHasAccessibility(Boolean hasAccessibility);
    public void setHasAudio(Boolean hasAudio);
    public void setHasAudioEncoder(Boolean hasAudioEncoder);
    public void setHasEmbeddedVideo(Boolean hasEmbeddedVideo);
    public void setHasIME(Boolean hasIME);
    public void setHasMP3(Boolean hasMP3);
    public void setHasPrinting(Boolean hasPrinting);
    public void setHasScreenBroadcast(Boolean hasScreenBroadcast);
    public void setHasScreenPlayback(Boolean hasScreenPlayback);
    public void setHasStreamingAudio(Boolean hasStreamingAudio);
    public void setHasStreamingVideo(Boolean hasStreamingVideo);
    public void setHasTLS(Boolean hasTLS);
    public void setHasVideoEncoder(Boolean hasVideoEncoder);
    public void setIsDebugger(Boolean isDebugger);
    public void setLanguage(String language);
    public void setLocalFileReadDisable(Boolean localFileReadDisable);
    public void setManufacturer(String manufacturer);
    public void setMaxLevelIDC(String maxLevelIDC);
    public void setOs(String os);
    public void setPixelAspectRatio(Float pixelAspectRatio);
    public void setPlayerType(String playerType);
    public void setScreenColor(String screenColor);
    public void setScreenDPI(Integer screenDPI);
    public void setScreenResolutionX(Integer screenResolutionX);
    public void setScreenResolutionY(Integer screenResolutionY);
    public void setSupports32BitProcesses(Boolean supports32BitProcesses);
    public void setSupports64BitProcesses(Boolean supports64BitProcesses);
    public void setTouchscreenType(String touchscreenType);
    public void setVersion(String version);
    public void setLsoStorageTest(Boolean lsoStorageTest);
}
