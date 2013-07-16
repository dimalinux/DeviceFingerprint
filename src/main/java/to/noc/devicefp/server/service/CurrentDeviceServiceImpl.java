/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static to.noc.devicefp.server.util.IpUtil.ipWithHostName;
import static to.noc.devicefp.server.util.IpUtil.ipToHostName;
import to.noc.devicefp.server.domain.entity.*;
import to.noc.devicefp.server.domain.repository.DeviceRepository;
import to.noc.devicefp.server.domain.repository.OpenIdUserRepository;
import to.noc.devicefp.server.domain.repository.ZombieCookieRepository;
import to.noc.devicefp.shared.CookieType;
import static to.noc.devicefp.shared.CookieDefs.DEVICE_COOKIE_NAME;

@Service /* Spring service */
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentDeviceServiceImpl extends AbstractCookieStatesUpdater implements CurrentDeviceService {

    private enum SpecialCookieType { FLASH, WEB_STORAGE };

    private static final Logger log = LoggerFactory.getLogger(CurrentDeviceServiceImpl.class);

    @Autowired private DeviceRepository deviceRepository;
    @Autowired private OpenIdUserRepository userRepository;
    @Autowired private ZombieCookieRepository cookieRepository;
    @Autowired private CurrentUserService currentUserService;
    @Autowired private TcpdumpService tcpdumpService;
    @Autowired private MaxMindService maxMindService;
    @Autowired private HttpServletRequest request;

    private Device currentDevice;

    @Override
    public synchronized Long getDeviceId() {
        return (currentDevice != null) ? currentDevice.getId() : null;
    }

    @Override
    public synchronized TcpSynData getTcpSynData() {
        return (currentDevice != null) ? currentDevice.getTcpSynData(): null;
    }

    @Override
    public synchronized DnsData getDnsData() {
        return (currentDevice != null) ? currentDevice.getDnsData() : null;
    }

    @Override
    public synchronized Date getPageLoadStamp() {
        return (currentDevice != null) ? currentDevice.getServerStamp() : null;
    }

    /*
     *  Methods that modify cookies all have multiple database interactions and
     *  should be placed in a transaction.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public synchronized Device fixateNewDevice(Device newDevice, String existingPlainCookieId, String etagId) {
        setServerEndStamp();  // sets end stamp on previous device if there was one

        // can't do initial save until cookie is set (cookie_id is non null)
        currentDevice = newDevice;
        initCookieStates(existingPlainCookieId, etagId);
        currentDevice = deviceRepository.save(newDevice);

        loadTcpSynData();

        return currentDevice;
    }


    /*  This method is called when we fixate a new device, but the SYN data may
     *  be delayed and not ready yet, so the method is called again from the
     *  DnsJsController.
     */
    @Transactional
    @Override
    public synchronized TcpSynData loadTcpSynData() {
        TcpSynData synData = currentDevice.getTcpSynData();
        if (synData == null) {
            String ip = currentDevice.getIpAddress();
            int port = currentDevice.getRemotePort();
            synData = tcpdumpService.getTcpSynData(ip, port);
            if (synData != null) {
                ensureAttached();
                currentDevice.setTcpSynData(synData);
            }
        }
        return currentDevice.getTcpSynData();
    }


    @Transactional
    @Override
    public synchronized DnsData loadDnsData(String requestedDnsKey) {

        DnsData dnsData = currentDevice.getDnsData();
        if (dnsData != null) {
            log.debug("loadDnsData returning cached copy of data");
            return dnsData;
        }

        dnsData = tcpdumpService.getDnsData(currentDevice, requestedDnsKey);
        if (dnsData == null) {
            return null;
        }

        String ipAddress = dnsData.getSourceIp();
        if (dnsData.getHostName() == null) {
            dnsData.setHostName(ipToHostName(ipAddress));
        }

        if (dnsData.getMaxMindLocation() == null) {
            dnsData.setMaxMindLocation(maxMindService.loadLocation(ipAddress));
        }

        ensureAttached();
        currentDevice.setDnsData(dnsData);

        return currentDevice.getDnsData();
    }

    /*
     *  Methods called from client are asynchronous calls to the server and
     *  can occur simultaneously. Since this service is session scope, the
     *  method level synchronization statements should be okay.
     */
    @Override
    @Transactional
    public synchronized CookieStates saveImmediateValues(
            JsData jsData,
            DisplayData displayData,
            List<Plugin> plugins,
            String webStorageCookieId) {

        if (hasCurrentDevice("saveImmediateValues")) {

            ensureAttached();

            boolean updateJs = currentDevice.getJsData() == null && jsData != null;
            boolean updateDisplay = currentDevice.getDisplayData() == null && displayData != null;
            boolean updatePlugins = currentDevice.getPlugins().isEmpty() && plugins != null;

            if (updateJs) {
                currentDevice.setJsData(jsData);
                updateCookieStates(jsData, wasDeviceCookieSent());
                updateCookieStates(CookieType.WEB_STORAGE, webStorageCookieId);
            }
            if (updateDisplay) {
                currentDevice.setDisplayData(displayData);
            }
            if (updatePlugins) {
                currentDevice.setPlugins(plugins);
            }

            return currentDevice.getCookieStates();
        }

        return null;
    }

    @Override
    @Transactional
    public synchronized CookieStates saveSilverlightData(SilverlightData silverlightData, String silverlightCookieId) {
        if (hasCurrentDevice("saveSilverlightData")) {
            if (currentDevice.getSilverlightData() == null && silverlightData != null) {
                ensureAttached();
                currentDevice.setSilverlightData(silverlightData);
                updateCookieStates(silverlightData);
                updateCookieStates(CookieType.SILVERLIGHT, silverlightCookieId);
            }
            return currentDevice.getCookieStates();
        }
        return null;
    }

    @Override
    @Transactional
    public synchronized CookieStates saveFlashData(FlashData flashData, String flashCookieId) {
        if (hasCurrentDevice("saveFlashData")) {
            if (currentDevice.getFlashData() == null && flashData != null) {
                ensureAttached();
                currentDevice.setFlashData(flashData);
                updateCookieStates(flashData);
                updateCookieStates(CookieType.FLASH, flashCookieId);
            }
            return currentDevice.getCookieStates();
        }
        return null;
    }

    @Override
    @Transactional
    public synchronized void saveJavaData(JavaData javaData) {
        if (hasCurrentDevice("saveJavaData") && currentDevice.getJavaData() == null && javaData != null) {
            ensureAttached();
            currentDevice.setJavaData(javaData);
        }
    }

    @Override
    @Transactional
    public synchronized void saveBrowserLocation(BrowserLocation browserLocation) {
        if (hasCurrentDevice("saveBrowserLocation") && currentDevice.getBrowserLocation() == null && browserLocation != null) {
            ensureAttached();
            currentDevice.setBrowserLocation(browserLocation);
        }
    }

    @Override
    @Transactional
    public synchronized void saveReverseGeocode(ReverseGeocode reverseGeocode) {
        if (hasCurrentDevice("saveReverseGeocode") && currentDevice.getReverseGeocode() == null && reverseGeocode != null) {
            ensureAttached();
            currentDevice.setReverseGeocode(reverseGeocode);
        }
    }

    @Override
    public synchronized String getCookieId() {
        return hasCurrentDevice("getCookieId") ?
             currentDevice.getZombieCookie().getId() : null;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    protected synchronized CookieStates getCookieStates() {
        CookieStates cookieStates = currentDevice.getCookieStates();
        if (cookieStates == null) {
            cookieStates = new CookieStates();
            currentDevice.setCookieStates(cookieStates);
        }
        return currentDevice.getCookieStates();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    protected ZombieCookie lookupZombieCookie(String id) {
        ZombieCookie cookie = cookieRepository.findOne(id);
        if (cookie != null) {
            Date timeStamp = getPageLoadStamp();
            Date lastSeen = cookie.getLastSeen();
            if (lastSeen == null || lastSeen.before(timeStamp)) {
                cookie.setLastSeen(timeStamp);
            }
            if (currentUserService.isAuthenticated()) {
                // link an attached, up-to-date version
                Long userId = currentUserService.getUserId();
                cookie.linkTo(userRepository.findOne(userId));
            }
        }
        return cookie;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    protected ZombieCookie createZombieCookie() {
        // setting inception date equal to page load timestamp
        ZombieCookie cookie = cookieRepository.save(new ZombieCookie(getPageLoadStamp()));

        if (currentUserService.isAuthenticated()) {
            Long userId = currentUserService.getUserId();
            cookie.linkTo(userRepository.findOne(userId));
        }

        return cookie;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    protected synchronized ZombieCookie getZombieCookie() {
        return currentDevice.getZombieCookie();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    protected synchronized ZombieCookie setAndSaveZombieCookie(ZombieCookie cookie) {
        //cookie = cookieRepository.save(cookie);
        currentDevice.setZombieCookie(cookie);
        return cookie;
    }

    private boolean wasDeviceCookieSent() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : request.getCookies()) {
                if (DEVICE_COOKIE_NAME.equals(c.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public synchronized void windowClosed(Date windowCloseTimestamp) {
        log.info("Window close event received");

        if (hasCurrentDevice("windowClosed")) {
            ensureAttached();
            currentDevice.setServerEndStamp(new Date());
            JsData jsData = currentDevice.getJsData();
            if (jsData != null) {
                jsData.setWindowCloseStamp(windowCloseTimestamp);
            }
        }
    }

    /*
     *  The primary purpose of client keepSessionAlive calls is to keep
     *  the current device alive in session storage, so I put the method
     *  here for lack any better place.
     */
    @Override
    public synchronized Boolean keepSessionAlive() {
        boolean alive = hasCurrentDevice();

        if (alive) {
            log.info("Session keep-alive requested deviceId={} client={}",
                    currentDevice.getId(), getClientInfo());
        } else {
            alive = false;
            log.warn("Session keep-alive requested, with no current device: client={}",
                        getClientInfo());
        }
        return alive;
    }


    @Transactional(propagation = Propagation.MANDATORY)
    private synchronized void ensureAttached() {
        // Spring-data-jpa will call merge on the entity manager for saves
        // on an existing entity.  Any changes to an attached entity are
        // fixated when the transaction commits.
        currentDevice = deviceRepository.save(currentDevice);
    }


    // Returns true if the home page controller persisted a device in the current
    // HTTP session.
    @Override
    public synchronized boolean hasCurrentDevice() {
        return currentDevice != null;
    }

    // same as above but private with logging
    private synchronized boolean hasCurrentDevice(String callerName) {
        boolean doesExist = currentDevice != null;
        if (!doesExist) {
            log.warn("{} called with no current device: client={}",
                        callerName, getClientInfo());
        }
        return doesExist;
    }

    @Transactional
    private synchronized void setServerEndStamp() {
        // Server end stamp may have already been set by the windowClosed()
        if (currentDevice != null && currentDevice.getServerEndStamp() == null) {
            ensureAttached();
            currentDevice.setServerEndStamp(new Date());
        }
    }

    // result is used for logging
    @Override
    public synchronized String getClientInfo() {
        return currentDevice != null ?
                currentDevice.getClientIpInfo() :
                ipWithHostName(RequestFactoryServlet.getThreadLocalRequest().getRemoteAddr());
    }

}
