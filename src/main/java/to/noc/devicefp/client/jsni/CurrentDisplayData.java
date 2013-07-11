/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JavaScriptObject;
import to.noc.devicefp.client.entity.DisplayDataCs;

public final class CurrentDisplayData implements DisplayDataCs {

    private static CurrentDisplayData instance;

    public static CurrentDisplayData instance() {
        if (instance == null) {
            instance = new CurrentDisplayData();
        }
        return instance;
    }

    // In some cases this is of type NativeEvent, other times it is of type
    // Touch (hence the generic JavaScriptObject declaration).  Both object
    // types have the same mouse coordinate values.  Initializing to empty
    // object so we never have to check for null.
    private static JavaScriptObject lastEvent = JavaScriptObject.createObject();

    private CurrentDisplayData() {
    }

    // It's not safe to save a reference to the current event, so we copy into
    // our variable "lastEvent".  Method returns true if the new value differs
    // from the previous value, otherwise false.
    //
    // TBD:
    // On mobile Chrome, mouse events are giving me screenX/Y values of zero,
    // while touch events give non-zero values.  Should we figure out a way
    // to ignore screenX/Y for non-touch events on mobile Chrome?
    //
    public native boolean setLastEvent(JavaScriptObject event) /*-{
        var le = @to.noc.devicefp.client.jsni.CurrentDisplayData::lastEvent;
        var valueChanged = false;

        if (le.clientX !== event.clientX ||
            le.clientY !== event.clientY ||
            le.screenX !== event.screenX ||
            le.screenY !== event.screenY ||
            le.pageX   !== event.pageX   ||
            le.pageY   !== event.pageY      ) {

            le.clientX = event.clientX;
            le.clientY = event.clientY;
            le.screenX = event.screenX;
            le.screenY = event.screenY;
            le.pageX   = event.pageX;
            le.pageY   = event.pageY;

            valueChanged = true;
        }
        //console.log("screenX/Y: ", le.screenX, "/", le.screenY, " type=", event.type);
        return valueChanged;
    }-*/;
    
    @Override
    public native Integer getWidth() /*-{
        var value = screen.width;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getHeight() /*-{
        var value = screen.height;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getAvailWidth() /*-{
        var value = screen.availWidth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getAvailHeight() /*-{
        var value = screen.availHeight;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getColorDepth() /*-{
        var value = screen.colorDepth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getPixelDepth() /*-{
        var value = screen.pixelDepth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Float getPixelRatio() /*-{
        var value = $wnd.devicePixelRatio;
        return (typeof value == 'number') ? @java.lang.Float::valueOf(F)(value) : null;
    }-*/;

    @Override
    public native Integer getInnerWidth() /*-{
        var value = $wnd.innerWidth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getInnerHeight() /*-{
        var value = $wnd.innerHeight;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getOuterWidth() /*-{
        var value = $wnd.outerWidth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getOuterHeight() /*-{
        var value = $wnd.outerHeight;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getPageXOffset() /*-{
        var value = $wnd.pageXOffset || $doc.documentElement.scrollLeft || $doc.body.scrollLeft;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getPageYOffset() /*-{
        var value = $wnd.pageYOffset || $doc.documentElement.scrollTop || $doc.body.scrollTop;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getClientWidth() /*-{
        var value = $doc.documentElement.clientWidth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getClientHeight() /*-{
        var value = $doc.documentElement.clientHeight;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getOffsetWidth() /*-{
        var value = $doc.documentElement.offsetWidth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getOffsetHeight() /*-{
        var value = $doc.documentElement.offsetHeight;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getScreenX() /*-{
        var value = $wnd.screenX || $wnd.screenLeft;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getScreenY() /*-{
        var value = $wnd.screenY || $wnd.screenTop;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Boolean getFontSmoothingEnabled() /*-{
        var value = screen.fontSmoothingEnabled;
        return (typeof value == 'boolean') ? @java.lang.Boolean::valueOf(Z)(value) : null;
    }-*/;

    @Override
    public native Integer getBufferDepth() /*-{
        var value = screen.bufferDepth;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getDeviceXDPI() /*-{
        var value = screen.deviceXDPI;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getDeviceYDPI() /*-{
        var value = screen.deviceYDPI;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getLogicalXDPI() /*-{
        var value = screen.logicalXDPI;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getLogicalYDPI() /*-{
        var value = screen.logicalYDPI;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getSystemXDPI() /*-{
        var value = screen.systemXDPI;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getSystemYDPI() /*-{
        var value = screen.systemYDPI;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    @Override
    public native Integer getUpdateInterval() /*-{
        var value = screen.updateInterval;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    public native Integer getMouseClientX() /*-{
        var event = @to.noc.devicefp.client.jsni.CurrentDisplayData::lastEvent;
        var value = event.clientX;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    public native Integer getMouseClientY() /*-{
        var event = @to.noc.devicefp.client.jsni.CurrentDisplayData::lastEvent;
        var value = event.clientY;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    public native Integer getMouseScreenX() /*-{
        var event = @to.noc.devicefp.client.jsni.CurrentDisplayData::lastEvent;
        var value = event.screenX;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    public native Integer getMouseScreenY() /*-{
        var event = @to.noc.devicefp.client.jsni.CurrentDisplayData::lastEvent;
        var value = event.screenY;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    private native Integer getNativeMousePageX() /*-{
        var event = @to.noc.devicefp.client.jsni.CurrentDisplayData::lastEvent;
        var value = event.pageX;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    private native Integer getNativeMousePageY() /*-{
        var event = @to.noc.devicefp.client.jsni.CurrentDisplayData::lastEvent;
        var value = event.pageY;
        return (typeof value == 'number') ? @java.lang.Integer::valueOf(I)(value) : null;
    }-*/;

    public Integer getMousePageX() {
        Integer pageX = getNativeMousePageX();
        if (pageX == null) {
            Integer clientX = getMouseClientX();
            Integer screenX = getScreenX();
            if (clientX != null && screenX != null) {
                pageX = clientX + screenX;
            }
        }
        return pageX;
    }

    public Integer getMousePageY() {
        Integer pageY = getNativeMousePageY();
        if (pageY == null) {
            Integer clientY = getMouseClientY();
            Integer screenY = getScreenY();
            if (clientY != null && screenY != null) {
                pageY = clientY + screenY;
            }
        }
        return pageY;
    }
}
