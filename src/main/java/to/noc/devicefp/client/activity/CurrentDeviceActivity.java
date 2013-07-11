/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
 package to.noc.devicefp.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import to.noc.devicefp.client.entity.*;
import to.noc.devicefp.client.jsni.*;
import to.noc.devicefp.client.request.CurrentDeviceRequest;
import to.noc.devicefp.client.ui.DeviceDetailsView;
import to.noc.devicefp.client.ui.SessionLost;
import to.noc.devicefp.shared.AppRequestFactory;
import static to.noc.devicefp.shared.CookieDefs.DEVICE_COOKIE_MAX_AGE_MS;
import static to.noc.devicefp.shared.CookieDefs.DEVICE_COOKIE_NAME;
import to.noc.devicefp.shared.CookieState;
import static to.noc.devicefp.shared.CookieState.NOT_SUPPORTED;

public class CurrentDeviceActivity extends AbstractActivity
        implements PluginHandler.AppletCallback,
                   PluginHandler.FlashCallback,
                   PluginHandler.SilverlightCallback,
                   Html5Geolocation.Callback,
                   ReverseGeocoder.Callback
{
    private static final Logger log = Logger.getLogger(CurrentDeviceActivity.class.getName());
    private final AppRequestFactory requestFactory;
    private final DeviceDetailsView deviceDetailsView = DeviceDetailsView.instance();
    private final CurrentDevice device = CurrentDevice.instance();
    private final CurrentDisplayData displayData = CurrentDisplayData.instance();
    private boolean viewInitialized = false;

    public CurrentDeviceActivity(AppRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        loadCurrentDevice();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        deviceDetailsView.setDeleteHandler(null);
        deviceDetailsView.setDevice(device, true);
        viewInitialized = true;
        panel.setWidget(deviceDetailsView);
    }

    @Override
    public void onStop() {
        // Stop event handling if we leave the Current device tab.  Event
        // handling is started by DeviceDetailsView, since it knows when the
        // display section is opened and closed.
        CurrentDeviceDisplayEventHandling.instance().stopEventHandling();

        viewInitialized = false;
    }

    @Override
    public void onCancel() {
        onStop();
    }

    private void loadCurrentDevice() {
        String cookieId = device.getCookieStates().getZombieCookie().getId();
        CurrentDeviceRequest initRequest = requestFactory.currentDeviceRequest();

        Html5Geolocation.getCurrentPosition(this); // safe even if no support

        /*   It pains me to remove a feature that I invested time in, but the
         *   current warnings for unsigned Java applets scare users.  I won't
         *   gut the code for now in case applets become viable again.
         */
        //if (NavigatorFull.getJavaEnabled()) {
        //    PluginHandler.loadApplet(this);
        //}

        PluginHandler.loadFlash(this, cookieId);
        PluginHandler.loadSilverlight(this);

        final JsDataProxy jsProxy = initRequest.create(JsDataProxy.class);
        loadJsData(jsProxy);
        device.setJsData(jsProxy);

        final DisplayDataProxy displayProxy = initRequest.create(DisplayDataProxy.class);
        loadDisplayData(displayProxy);

        String webLocalStorageCookieId = null;
        boolean hasWebLocalStorage = Html5Storage.localStorageSupported();
        jsProxy.setWebLocalStorageCapable(hasWebLocalStorage);
        if (hasWebLocalStorage) {
            boolean readWriteTestPassed = Html5Storage.localStorageTest();
            jsProxy.setWebLocalStorageTest(readWriteTestPassed);
            // attempt read-only (just in case) even if the read/write test failed
            webLocalStorageCookieId = Html5Storage.readWebLocalStorageCookie();
            if (webLocalStorageCookieId == null && readWriteTestPassed) {
                Html5Storage.setWebLocalStorageCookie(cookieId);
            }
        }

        List<PluginProxy> plugins = loadPluginData(initRequest);

        initRequest.saveImmediateValues(jsProxy, displayProxy, plugins, webLocalStorageCookieId).fire(
                new Receiver<CookieStatesProxy>() {
                    @Override
                    public void onSuccess(CookieStatesProxy cookieStates) {
                        log.fine("success saving javascript data");
                        updateCookieValues(cookieStates);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        String msg = "failed saving javascript data: " + error.getMessage();
                        log.severe(msg);
                        if (!GWT.isProdMode()) {
                            Window.alert(msg);
                        }
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            String msg = "constraint violation: " + vio.getMessage();
                            log.severe(msg);
                        }
                    }
                });
    }

    @SuppressWarnings("deprecation")
    private void loadJsData(JsDataProxy jsDataProxy) {
        Date date = new Date();
        jsDataProxy.setClientStamp(date);
        jsDataProxy.setUtcOffsetMin(-date.getTimezoneOffset());

        boolean cookiesEnabled = NavigatorFull.getCookiesEnabled();
        jsDataProxy.setCookiesEnabled(cookiesEnabled);
        if (cookiesEnabled) {
            jsDataProxy.setCookiesTest(NavigatorFull.getCookiesTest());
        }
        jsDataProxy.setJavaEnabled(NavigatorFull.getJavaEnabled());
        jsDataProxy.setGeolocationCapable(NavigatorFull.geolocationCapable());
        jsDataProxy.setProduct(NavigatorFull.getProduct());
        jsDataProxy.setProductSub(NavigatorFull.getProductSub());
        jsDataProxy.setAppCodeName(NavigatorFull.getAppCodeName());
        jsDataProxy.setAppName(NavigatorFull.getAppName());
        jsDataProxy.setAppVersion(NavigatorFull.getAppVersion());
        jsDataProxy.setAppMinorVersion(NavigatorFull.getAppMinorVersion());
        jsDataProxy.setPlatform(NavigatorFull.getPlatform());
        jsDataProxy.setUserAgent(NavigatorFull.getUserAgent());
        jsDataProxy.setVendor(NavigatorFull.getVendor());
        jsDataProxy.setVendorSub(NavigatorFull.getVendorSub());
        jsDataProxy.setLanguage(NavigatorFull.getLanguage());
        jsDataProxy.setUserLanguage(NavigatorFull.getUserLanguage());
        jsDataProxy.setBrowserLanguage(NavigatorFull.getBrowserLanguage());
        jsDataProxy.setSystemLanguage(NavigatorFull.getSystemLanguage());
        jsDataProxy.setCpuClass(NavigatorFull.getCpuClass());
        jsDataProxy.setOscpu(NavigatorFull.getOscpu());
        jsDataProxy.setBuildID(NavigatorFull.getBuildID());
        jsDataProxy.setDoNotTrack(NavigatorFull.getDoNotTrack());
        jsDataProxy.setMsDoNotTrack(NavigatorFull.getMsDoNotTrack());
    }

    private void loadDisplayData(DisplayDataProxy displayDataProxy) {
        displayDataProxy.setWidth(displayData.getWidth());
        displayDataProxy.setHeight(displayData.getHeight());
        displayDataProxy.setAvailWidth(displayData.getAvailWidth());
        displayDataProxy.setAvailHeight(displayData.getAvailHeight());
        displayDataProxy.setColorDepth(displayData.getColorDepth());
        displayDataProxy.setPixelDepth(displayData.getPixelDepth());
        displayDataProxy.setPixelRatio(displayData.getPixelRatio());
        displayDataProxy.setInnerWidth(displayData.getInnerWidth());
        displayDataProxy.setInnerHeight(displayData.getInnerHeight());
        displayDataProxy.setOuterWidth(displayData.getOuterWidth());
        displayDataProxy.setOuterHeight(displayData.getOuterHeight());
        displayDataProxy.setPageXOffset(displayData.getPageXOffset());
        displayDataProxy.setPageYOffset(displayData.getPageYOffset());
        displayDataProxy.setClientWidth(displayData.getClientWidth());
        displayDataProxy.setClientHeight(displayData.getClientHeight());
        displayDataProxy.setOffsetWidth(displayData.getOffsetWidth());
        displayDataProxy.setOffsetHeight(displayData.getOffsetHeight());
        displayDataProxy.setScreenX(displayData.getScreenX());
        displayDataProxy.setScreenY(displayData.getScreenY());
        displayDataProxy.setFontSmoothingEnabled(displayData.getFontSmoothingEnabled());
        displayDataProxy.setBufferDepth(displayData.getBufferDepth());
        displayDataProxy.setDeviceXDPI(displayData.getDeviceXDPI());
        displayDataProxy.setDeviceYDPI(displayData.getDeviceYDPI());
        displayDataProxy.setLogicalXDPI(displayData.getLogicalXDPI());
        displayDataProxy.setLogicalYDPI(displayData.getLogicalYDPI());
        displayDataProxy.setSystemXDPI(displayData.getSystemXDPI());
        displayDataProxy.setSystemYDPI(displayData.getSystemYDPI());
        displayDataProxy.setUpdateInterval(displayData.getUpdateInterval());
    }

    @Override
    public void setReverseGeocodeAddress(ReverseGeocodeJso reverseGeocodeData) {
        CurrentDeviceRequest request = requestFactory.currentDeviceRequest();
        ReverseGeocodeProxy reverseGeocodeProxy = request.create(ReverseGeocodeProxy.class);

        device.setReverseGeocode(reverseGeocodeData);
        if (viewInitialized) {
            deviceDetailsView.refreshBrowserLocation();
        }

        reverseGeocodeProxy.setStatus(reverseGeocodeData.getStatus());
        reverseGeocodeProxy.setLatitude(reverseGeocodeData.getLatitude());
        reverseGeocodeProxy.setLongitude(reverseGeocodeData.getLongitude());
        reverseGeocodeProxy.setAddress(reverseGeocodeData.getAddress());
        reverseGeocodeProxy.setAddressType(reverseGeocodeData.getAddressType());
        reverseGeocodeProxy.setStreetNumber(reverseGeocodeData.getStreetNumber());
        reverseGeocodeProxy.setRoute(reverseGeocodeData.getRoute());
        reverseGeocodeProxy.setNeighborhood(reverseGeocodeData.getNeighborhood());
        reverseGeocodeProxy.setLocality(reverseGeocodeData.getLocality());
        reverseGeocodeProxy.setAdministrativeAreaLevel1(reverseGeocodeData.getAdministrativeAreaLevel1());
        reverseGeocodeProxy.setAdministrativeAreaLevel2(reverseGeocodeData.getAdministrativeAreaLevel2());
        reverseGeocodeProxy.setCountry(reverseGeocodeData.getCountry());
        reverseGeocodeProxy.setPostalCode(reverseGeocodeData.getPostalCode());

        request.saveReverseGeocode(reverseGeocodeProxy).fire(
                new Receiver<Void>() {

                    @Override
                    public void onSuccess(Void notUsed) {
                        log.fine("success saving reverse geocode data");
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        String msg = "saving reverse geocode data failed: " + error.getMessage();
                        log.severe(msg);
                        if (!GWT.isProdMode()) {
                            Window.alert(msg);
                        }
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            String msg = "reverse geocode data constraint violation: " + vio.getMessage();
                            log.warning(msg);
                        }
                    }
                });
    }


    @Override
    public void setBrowserLocation(BrowserLocationJso locationData) {

        CurrentDeviceRequest request = requestFactory.currentDeviceRequest();
        BrowserLocationProxy locationProxy = request.create(BrowserLocationProxy.class);

        device.setBrowserLocation(locationData);
        if (viewInitialized) {
            deviceDetailsView.refreshBrowserLocation();
        }

        locationProxy.setStamp(locationData.getStamp());
        Double latitude = locationData.getLatitude();
        Double longitude = locationData.getLongitude();
        locationProxy.setLatitude(latitude);
        locationProxy.setLongitude(longitude);
        if (latitude != null && longitude != null) {
            ReverseGeocoder.computeAddress(this, latitude, longitude);
        }
        locationProxy.setAccuracyRadius(locationData.getAccuracyRadius());
        locationProxy.setAltitude(locationData.getAltitude());
        locationProxy.setAltitudeAccuracy(locationData.getAltitudeAccuracy());
        locationProxy.setSpeed(locationData.getSpeed());
        locationProxy.setHeading(locationData.getHeading());
        locationProxy.setErrorCode(locationData.getErrorCode());
        locationProxy.setErrorMessage(locationData.getErrorMessage());

        request.saveBrowserLocation(locationProxy).fire(
                new Receiver<Void>() {

                    @Override
                    public void onSuccess(Void notUsed) {
                        log.fine("success saving location data");
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        String msg = "saving location data failed: " + error.getMessage();
                        log.severe(msg);
                        if (!GWT.isProdMode()) {
                            Window.alert(msg);
                        }
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            String msg = "location data constraint violation: " + vio.getMessage();
                            log.warning(msg);
                        }
                    }
                });
    }

    @Override
    public void setJavaData(
            String javaVendor,
            String javaVendorUrl,
            String javaVersion,
            String osArch,
            String osName,
            String osVersion,
            String lanIpAddress
    ) {
        CurrentDeviceRequest request = requestFactory.currentDeviceRequest();
        JavaDataProxy javaData = request.create(JavaDataProxy.class);

        javaData.setJavaVendor(javaVendor);
        javaData.setJavaVendorUrl(javaVendorUrl);
        javaData.setJavaVersion(javaVersion);
        javaData.setOsArch(osArch);
        javaData.setOsName(osName);
        javaData.setOsVersion(osVersion);
        javaData.setLanIpAddress(lanIpAddress);

        device.setJavaData(javaData);
        // We are in an asynchronous callback.  Reset the view if it has already
        // been initialized without the java data
        if (viewInitialized) {
            deviceDetailsView.refreshJavaData();
        }

        request.saveJavaData(javaData).fire(
                new Receiver<Void>() {

                    @Override
                    public void onSuccess(Void notUsed) {
                        log.fine("success saving java data");
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        String msg = "saving java data failed: " + error.getMessage();
                        log.severe(msg);
                        if (!GWT.isProdMode()) {
                            Window.alert(msg);
                        }
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            String msg = "java data constraint violation: " + vio.getMessage();
                            log.warning(msg);
                        }
                    }
                });
    }


    @Override
    public void setFlashData(FlashDataJso flashData) {

        device.setFlashData(flashData);
        // We are in an asynchronous callback.  Reset the view if it has already
        // been initilized without the flash data
        if (viewInitialized) {
            deviceDetailsView.refreshFlashData();
        }

        CurrentDeviceRequest request = requestFactory.currentDeviceRequest();
        FlashDataProxy flashDataProxy = request.create(FlashDataProxy.class);

        flashDataProxy.setAvHardwareDisable(flashData.getAvHardwareDisable());
        flashDataProxy.setCpuArchitecture(flashData.getCpuArchitecture());
        flashDataProxy.setHasAccessibility(flashData.getHasAccessibility());
        flashDataProxy.setHasAudio(flashData.getHasAudio());
        flashDataProxy.setHasAudioEncoder(flashData.getHasAudioEncoder());
        flashDataProxy.setHasEmbeddedVideo(flashData.getHasEmbeddedVideo());
        flashDataProxy.setHasIME(flashData.getHasIME());
        flashDataProxy.setHasMP3(flashData.getHasMP3());
        flashDataProxy.setHasPrinting(flashData.getHasPrinting());
        flashDataProxy.setHasScreenBroadcast(flashData.getHasScreenBroadcast());
        flashDataProxy.setHasScreenPlayback(flashData.getHasScreenPlayback());
        flashDataProxy.setHasStreamingAudio(flashData.getHasStreamingAudio());
        flashDataProxy.setHasStreamingVideo(flashData.getHasStreamingVideo());
        flashDataProxy.setHasTLS(flashData.getHasTLS());
        flashDataProxy.setHasVideoEncoder(flashData.getHasVideoEncoder());
        flashDataProxy.setIsDebugger(flashData.getIsDebugger());
        flashDataProxy.setLanguage(flashData.getLanguage());
        flashDataProxy.setLocalFileReadDisable(flashData.getLocalFileReadDisable());
        flashDataProxy.setManufacturer(flashData.getManufacturer());
        flashDataProxy.setMaxLevelIDC(flashData.getMaxLevelIDC());
        flashDataProxy.setOs(flashData.getOs());
        flashDataProxy.setPixelAspectRatio(flashData.getPixelAspectRatio());
        flashDataProxy.setPlayerType(flashData.getPlayerType());
        flashDataProxy.setScreenColor(flashData.getScreenColor());
        flashDataProxy.setScreenDPI(flashData.getScreenDPI());
        flashDataProxy.setScreenResolutionX(flashData.getScreenResolutionX());
        flashDataProxy.setScreenResolutionY(flashData.getScreenResolutionY());
        flashDataProxy.setSupports32BitProcesses(flashData.getSupports32BitProcesses());
        flashDataProxy.setSupports64BitProcesses(flashData.getSupports64BitProcesses());
        flashDataProxy.setTouchscreenType(flashData.getTouchscreenType());
        flashDataProxy.setVersion(flashData.getVersion());
        flashDataProxy.setLsoStorageTest(flashData.getLsoStorageTest());

        String flashCookieId = flashData.getExistingCookieId();

        request.saveFlashData(flashDataProxy, flashCookieId).fire(
                new Receiver<CookieStatesProxy>() {

                    @Override
                    public void onSuccess(CookieStatesProxy cookieStates) {
                        log.fine("success saving flash data");
                        updateCookieValues(cookieStates);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        String msg = "saving flash data failed: " + error.getMessage();
                        log.severe(msg);
                        if (!GWT.isProdMode()) {
                            Window.alert(msg);
                        }
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            String msg = "flash data constraint violation: " + vio.getMessage();
                            log.warning(msg);
                        }
                    }
                });
    }

    @Override
    public void setSilverlightData(SilverlightDataJso silverlightData) {

        device.setSilverlightData(silverlightData);
        // We are in an asynchronous callback.  Reset the view if it has already
        // been initilized without the silverlight data
        if (viewInitialized) {
            deviceDetailsView.refreshSilverlightData();
        }

        String silverlightCookieId = null;
        if (silverlightData.getIsolatedStorageEnabled()) {
            silverlightCookieId = silverlightData.getCookieId();
        }

        CurrentDeviceRequest request = requestFactory.currentDeviceRequest();
        SilverlightDataProxy silverlightDataProxy = request.create(SilverlightDataProxy.class);
        silverlightDataProxy.setAssemblyClrVersion(silverlightData.getAssemblyClrVersion());
        silverlightDataProxy.setClrVersion(silverlightData.getClrVersion());
        silverlightDataProxy.setOsVersion(silverlightData.getOsVersion());
        silverlightDataProxy.setProcessorCount(silverlightData.getProcessorCount());
        silverlightDataProxy.setSysUptimeMs(silverlightData.getSysUptimeMs());
        silverlightDataProxy.setIsolatedStorageEnabled(silverlightData.getIsolatedStorageEnabled());
        silverlightDataProxy.setIsolatedStorageTest(silverlightData.getIsolatedStorageTest());

        request.saveSilverlightData(silverlightDataProxy, silverlightCookieId).fire(
                new Receiver<CookieStatesProxy>() {

                    @Override
                    public void onSuccess(CookieStatesProxy cookieStates) {
                        log.fine("success saving silverlight data");
                        updateCookieValues(cookieStates);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        String msg = "saving silverlight data failed: " + error.getMessage();
                        log.severe(msg);
                        if (!GWT.isProdMode()) {
                            Window.alert(msg);
                        }
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            String msg = "silverlight data constraint violation: " + vio.getMessage();
                            log.warning(msg);
                        }
                    }
                });
    }

    private List<PluginProxy> loadPluginData(CurrentDeviceRequest request) {
        List<PluginCs> plugins = device.getPlugins();
        List<PluginProxy> proxyPlugins = new ArrayList<PluginProxy>(plugins.size());
        for(int i = 0; i < plugins.size(); i++) {
            PluginProxy pluginProxy = request.create(PluginProxy.class);
            PluginCs plugin = plugins.get(i);
            pluginProxy.setName(plugin.getName());
            pluginProxy.setDescription(plugin.getDescription());
            pluginProxy.setVersion(plugin.getVersion());
            pluginProxy.setFilename(plugin.getFilename());
            proxyPlugins.add(pluginProxy);
        }
        return proxyPlugins;
    }

    private void updateCookieValues(CookieStatesProxy newCookieStates) {

        if (newCookieStates == null) {
            SessionLost.show();
            return;
        }

        CookieStatesCs oldCookieStates = device.getCookieStates();

        if (newCookieStates.getVersion() < oldCookieStates.getVersion()) {
            // Responses after flash, silverlight, etc. can can be
            // processed out of order
            log.fine("received cookie state responses out of order, skipping update");
            return;
        }

        String newCookieId = newCookieStates.getZombieCookie().getId();

        CookieState plainCookieState = newCookieStates.getPlainState();
        if (plainCookieState != oldCookieStates.getPlainState()) {
            if (NavigatorFull.getCookiesEnabled()) {
                Date expire = new Date(new Date().getTime() + DEVICE_COOKIE_MAX_AGE_MS);
                try {
                    Cookies.setCookie(DEVICE_COOKIE_NAME, newCookieId, expire);
                } catch (Throwable ex) {
                    log.log(Level.WARNING, "failed to write cookie", ex);
                }
            }
        }

        // Note: If the old state was null (i.e. not processed) and the new
        //       state is EXISTING, we still want to rewrite the cookie
        //       to update any timestamp
        CookieState wsCookieState = newCookieStates.getWebStorageState();
        if (wsCookieState != null
                && wsCookieState != NOT_SUPPORTED
                && wsCookieState != oldCookieStates.getWebStorageState()) {
            Html5Storage.setWebLocalStorageCookie(newCookieId);
        }

        CookieState flashCookieState = newCookieStates.getFlashState();
        if (flashCookieState != null
                && flashCookieState != NOT_SUPPORTED
                && flashCookieState != oldCookieStates.getFlashState()) {
                PluginHandler.setFlashCookie(newCookieId);
        }

        CookieState silverlightCookieState = newCookieStates.getSilverlightState();
        if (silverlightCookieState != null
                && silverlightCookieState != NOT_SUPPORTED
                && silverlightCookieState != oldCookieStates.getSilverlightState()) {
            device.getSilverlightData().setCookieId(newCookieId);
        }

        device.setCookieStates(newCookieStates);
        if (viewInitialized) {
            deviceDetailsView.refreshCookieValues();
        }
    }

}
