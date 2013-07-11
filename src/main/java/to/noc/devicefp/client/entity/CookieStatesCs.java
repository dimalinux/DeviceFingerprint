/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import to.noc.devicefp.shared.CookieState;

public interface CookieStatesCs {
    public CookieState getPlainState();
    public CookieState getEtagState();
    public CookieState getFlashState();
    public CookieState getWebStorageState();
    public CookieState getSilverlightState();
    public ZombieCookieCs getZombieCookie();
    public Integer getVersion();
}
