/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.FlashDataCs;

/* Unlike CurrentDevice and CurrentUser, this class is not implemented as
 * a singleton and is not available at page load time.
 */
public final class FlashDataJso extends JavaScriptObject implements FlashDataCs {

    protected FlashDataJso() {}

    @Override
    public native Boolean getAvHardwareDisable() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.avHardwareDisable);
    }-*/;

    @Override
    public native String getCpuArchitecture() /*-{
        return this.cpuArchitecture;
    }-*/;

    @Override
    public native Boolean getHasAccessibility() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasAccessibility);
    }-*/;

    @Override
    public native Boolean getHasAudio() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasAudio);
    }-*/;

    @Override
    public native Boolean getHasAudioEncoder() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasAudioEncoder);
    }-*/;

    @Override
    public native Boolean getHasEmbeddedVideo() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasEmbeddedVideo);
    }-*/;

    @Override
    public native Boolean getHasIME() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasIME);
    }-*/;

    @Override
    public native Boolean getHasMP3() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasMP3);
    }-*/;

    @Override
    public native Boolean getHasPrinting() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasPrinting);
    }-*/;

    @Override
    public native Boolean getHasScreenBroadcast() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasScreenBroadcast);
    }-*/;

    @Override
    public native Boolean getHasScreenPlayback() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasScreenPlayback);
    }-*/;

    @Override
    public native Boolean getHasStreamingAudio() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasStreamingAudio);
    }-*/;

    @Override
    public native Boolean getHasStreamingVideo() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasStreamingVideo);
    }-*/;

    @Override
    public native Boolean getHasTLS() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasTLS);
    }-*/;

    @Override
    public native Boolean getHasVideoEncoder() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.hasVideoEncoder);
    }-*/;


    @Override
    public native Boolean getIsDebugger() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.isDebugger);
    }-*/;

    @Override
    public native String getLanguage() /*-{
        return this.language;
    }-*/;

    @Override
    public native Boolean getLocalFileReadDisable() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.localFileReadDisable);
    }-*/;

    @Override
    public native String getManufacturer() /*-{
        return this.manufacturer;
    }-*/;

    @Override
    public native String getMaxLevelIDC() /*-{
        return this.maxLevelIDC;
    }-*/;

    @Override
    public native String getOs() /*-{
        return this.os;
    }-*/;

    @Override
    public native Float getPixelAspectRatio() /*-{
        return @java.lang.Float::valueOf(F)(this.pixelAspectRatio);
    }-*/;

    @Override
    public native String getPlayerType() /*-{
        return this.playerType;
    }-*/;

    @Override
    public native String getScreenColor() /*-{
        return this.screenColor;
    }-*/;

    @Override
    public native Integer getScreenDPI() /*-{
        return @java.lang.Integer::valueOf(I)(this.screenDPI);
    }-*/;

    @Override
    public native Integer getScreenResolutionX() /*-{
        return @java.lang.Integer::valueOf(I)(this.screenResolutionX);
    }-*/;

    @Override
    public native Integer getScreenResolutionY() /*-{
        return @java.lang.Integer::valueOf(I)(this.screenResolutionY);
    }-*/;

    @Override
    public native Boolean getSupports32BitProcesses() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.supports32BitProcesses);
    }-*/;

    @Override
    public native Boolean getSupports64BitProcesses() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.supports64BitProcesses);
    }-*/;

    @Override
    public native String getTouchscreenType() /*-{
        return this.touchscreenType;
    }-*/;

    @Override
    public native String getVersion() /*-{
        return this.version;
    }-*/;

    @Override
    public native Boolean getLsoStorageTest() /*-{
        return @java.lang.Boolean::valueOf(Z)(this.lsoStorageTest);
    }-*/;
    
    public native String getExistingCookieId() /*-{
        return this.existingCookieId;
    }-*/;
}
