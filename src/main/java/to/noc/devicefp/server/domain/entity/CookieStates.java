/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Parent;
import to.noc.devicefp.shared.CookieState;
import to.noc.devicefp.shared.CookieType;

@Embeddable
public class CookieStates implements Serializable {

    // Needed to share the device's version and the client-side uses
    // this class to access the device's zombieCookie.
    @Parent
    private Device device;

    @Enumerated(EnumType.STRING)
    private CookieState etagState;

    @Enumerated(EnumType.STRING)
    private CookieState plainState;

    @Enumerated(EnumType.STRING)
    private CookieState webStorageState;

    @Enumerated(EnumType.STRING)
    private CookieState flashState;

    @Enumerated(EnumType.STRING)
    private CookieState silverlightState;

    public Device getDevice() {
        return this.device;
    }

    void setDevice(Device device) {
        this.device = device;
    }

    public Integer getVersion() {
        return device.getVersion();
    }

    public CookieState getEtagState() {
        return etagState;
    }

    public void setEtagState(CookieState etagState) {
        this.etagState = etagState;
    }

    public CookieState getPlainState() {
        return plainState;
    }

    public void setPlainState(CookieState plainState) {
        this.plainState = plainState;
    }

    public CookieState getWebStorageState() {
        return webStorageState;
    }

    public void setWebStorageState(CookieState webStorageState) {
        this.webStorageState = webStorageState;
    }

    public CookieState getFlashState() {
        return flashState;
    }

    public void setFlashState(CookieState flashState) {
        this.flashState = flashState;
    }

    public CookieState getSilverlightState() {
        return silverlightState;
    }

    public void setSilverlightState(CookieState silverlightState) {
        this.silverlightState = silverlightState;
    }

    public CookieState get(CookieType type) {
        CookieState value = null;
        switch (type) {
            case ETAG:
                value = getEtagState();
                break;
            case PLAIN:
                value = getPlainState();
                break;
            case WEB_STORAGE:
                value = getWebStorageState();
                break;
            case FLASH:
                value = getFlashState();
                break;
            case SILVERLIGHT:
                value = getSilverlightState();
                break;
        }
        return value;
    }

    public void set(CookieType type, CookieState state) {
        switch (type) {
            case ETAG:
                setEtagState(state);
                break;
            case PLAIN:
                setPlainState(state);
                break;
            case WEB_STORAGE:
                setWebStorageState(state);
                break;
            case FLASH:
                setFlashState(state);
                break;
            case SILVERLIGHT:
                setSilverlightState(state);
                break;
        }
    }

    public ZombieCookie getZombieCookie() {
        return device.getZombieCookie();
    }

}
