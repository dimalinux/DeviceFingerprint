/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window.Navigator;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class NavigatorFull {
    private static final Logger log = Logger.getLogger(NavigatorFull.class.getName());

    private NavigatorFull() {}

    // I would have extended Window.Navagator for these first methods, but
    // it has a private constructor

    public static String getAppCodeName() {
        return Navigator.getAppCodeName();
    }

    public static String getAppName() {
        return Navigator.getAppName();
    }

    public static String getAppVersion() {
        return Navigator.getAppVersion();
    }

    public static String getPlatform() {
        return Navigator.getPlatform();
    }

    public static String getUserAgent() {
        return Navigator.getUserAgent();
    }

    public native static boolean getCookiesEnabled() /*-{
      return $wnd.navigator.cookieEnabled;
    }-*/;

    public static boolean getCookiesTest() {
        String name = "test_cookie";
        String value = "isEnabled";
        boolean readWiteSuccess = false;
        // The caching GWT test, Navigator.isCookieEnabled, is failing
        // on the latest IOS version under certain circumstances. This
        // version is working.
        try {
            Cookies.setCookie(name, value);
            readWiteSuccess = value.equals(Cookies.getCookie(name));
            Cookies.removeCookie(name);
        } catch (Throwable ex) {
            log.log(Level.WARNING, "exception in cookie test", ex);
        }
        return readWiteSuccess;
    }

    public static boolean getJavaEnabled() {
        return Navigator.isJavaEnabled();
    }

    public native static boolean geolocationCapable() /*-{
      return 'geolocation' in $wnd.navigator;
    }-*/;

    public native static String getProduct() /*-{
      return $wnd.navigator.product;
    }-*/;

    public native static String getProductSub() /*-{
      return $wnd.navigator.productSub;
    }-*/;

    public native static String getAppMinorVersion() /*-{
      return $wnd.navigator.appMinorVersion;
    }-*/;

    public native static String getVendor() /*-{
      return $wnd.navigator.vendor;
    }-*/;

    public native static String getVendorSub() /*-{
      return $wnd.navigator.vendorSub;
    }-*/;

    public native static String getLanguage() /*-{
      return $wnd.navigator.language;
    }-*/;

    public native static String getUserLanguage() /*-{
      return $wnd.navigator.userLanguage;
    }-*/;

    public native static String getBrowserLanguage() /*-{
      return $wnd.navigator.browserLanguage;
    }-*/;

    public native static String getSystemLanguage() /*-{
      return $wnd.navigator.systemLanguage;
    }-*/;

    public native static String getCpuClass() /*-{
      return $wnd.navigator.cpuClass;
    }-*/;

    public native static String getOscpu() /*-{
      return $wnd.navigator.oscpu;
    }-*/;

    public native static String getBuildID() /*-{
      return $wnd.navigator.buildID;
    }-*/;

    public native static String getDoNotTrack() /*-{
      return $wnd.navigator.doNotTrack;
    }-*/;

    /*
     * At this time, navigator.msDoNotTrack only has the values "0" or "1".
     * Should this be changed from Boolean to String in case Microsoft ever
     * changes the values?
     */
    public native static Boolean getMsDoNotTrack() /*-{
      var msDoNotTrack = $wnd.navigator.msDoNotTrack;
      return (typeof msDoNotTrack != 'undefined') ?
                @java.lang.Boolean::valueOf(Z)(msDoNotTrack == "1") : null;
    }-*/;

    public native static JsArray<PluginJso> getPlugins() /*-{
      return $wnd.navigator.plugins;
    }-*/;

}
