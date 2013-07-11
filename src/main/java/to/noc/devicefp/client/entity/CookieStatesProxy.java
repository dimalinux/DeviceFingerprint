/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.CookieStates;

@ProxyFor(value = CookieStates.class)
public interface CookieStatesProxy extends ValueProxy, CookieStatesCs {
    
    @Override
    public ZombieCookieProxy getZombieCookie();

}
