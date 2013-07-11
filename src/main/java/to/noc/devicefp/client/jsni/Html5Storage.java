/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

public final class Html5Storage {

    private static final String deviceCookieKey = "device";
    private static Boolean localStorageReadWriteTest = null;

    public native static boolean localStorageSupported() /*-{
        return true;
        try {
            return 'localStorage' in $wnd && $wnd['localStorage'] !== null;
        } catch(ex) {
            return false;
        }
    }-*/;


    /*
     * Reads and writes a test cookie to verify that web local storage
     * actuall works.
     */
    public static boolean localStorageTest() {
        // We cache the test status to avoid retests
        if (localStorageReadWriteTest == null) {
            String key = "session_rw_test";
            String value = "abcd1234";
            String readback = null;

            if (localStorageSupported()) {
                if (webLocalStorageSetItem(key, value)) {
                    readback = webLocalStorageGetItem(key);
                }
            }
            localStorageReadWriteTest = value.equals(readback);
        }
        return localStorageReadWriteTest;
    }

    public static boolean setWebLocalStorageCookie(String value) {
        return webLocalStorageSetItem(deviceCookieKey, value);
    }


    public static String readWebLocalStorageCookie() {
        return webLocalStorageGetItem(deviceCookieKey);
    }

    private native static boolean webLocalStorageSetItem(String key, String value) /*-{
        try {
            $wnd.localStorage.setItem(key, value);
            return true;
        } catch (ex) {
            return false;
        }
    }-*/;


    private native static String webLocalStorageGetItem(String key) /*-{
        try {
            return $wnd.localStorage.getItem(key);
        } catch (ex) {
            return null;
        }
    }-*/;
}
