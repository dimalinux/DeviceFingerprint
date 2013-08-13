/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

/*
 * GWT provides a wrapper to Javascript Geolocation, but this was simpler
 * for the functionality I wanted.
 */
public final class Html5Geolocation {
    public interface Callback {
        void setBrowserLocation(final BrowserLocationJso position);
    }

  public native static void getCurrentPosition(Callback callback) /*-{
    if ('geolocation' in $wnd.navigator) {

        var positionOptions = {
            "timeout": 30 * 1000,               // 30 seconds
            "maximumAge": 1 * 60 * 60 * 1000,   // 1 hr
            "enableHighAccuracy": true
        };

        var successOrFailCallback = function(result) {
            callback.@to.noc.devicefp.client.jsni.Html5Geolocation.Callback::setBrowserLocation(Lto/noc/devicefp/client/jsni/BrowserLocationJso;)(result);
        };

        if ($wnd.navigator.geolocation) {
            $wnd.navigator.geolocation.getCurrentPosition(
                successOrFailCallback,
                successOrFailCallback,
                positionOptions
            );
        } else {
           // The current (Aug. 2013) Tor browser bundle forced me to add this
           // extra check.  :(
           var result = {
                message: "Defective browser: navigator.geolocation exists, but is set to null"
           };
           successOrFailCallback(result);
        }

    }
  }-*/;

}
