/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import java.util.Date;
import java.util.List;
import to.noc.devicefp.server.domain.entity.*;


public interface CurrentDeviceService {

    Device fixateNewDevice(Device newDevice, String existingPlainCookieId, String etagId);

    String getClientInfo();

    String getCookieId();

    Long getDeviceId();

    DnsData getDnsData();

    Date getPageLoadStamp();

    TcpSynData getTcpSynData();

    /*
     * Returns true if the home page controller persisted a device in the current
     * HTTP session.
     */
    boolean hasCurrentDevice();

    DnsData loadDnsData(String requestedDnsKey);

    TcpSynData loadTcpSynData();

    //
    //  Remaining methods are all invoked by client side requests
    //
    CookieStates saveImmediateValues(JsData jsData, DisplayData displayData, List<Plugin> plugins, String webStorageCookieId);
    void saveBrowserLocation(BrowserLocation browserLocation);
    void saveReverseGeocode(ReverseGeocode reverseGeocode);
    CookieStates saveFlashData(FlashData flashData, String flashCookieId);
    void saveJavaData(JavaData javaData);
    CookieStates saveSilverlightData(SilverlightData silverlightData, String silverlightCookieId);
    void windowClosed(Date windowCloseTimestamp);
    Boolean keepSessionAlive();

}
