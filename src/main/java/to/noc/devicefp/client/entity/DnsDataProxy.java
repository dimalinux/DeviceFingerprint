/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.DnsData;

@ProxyFor(value = DnsData.class)
public interface DnsDataProxy extends ValueProxy, DnsDataCs {
    @Override
    public MaxMindLocationProxy getMaxMindLocation();
}
