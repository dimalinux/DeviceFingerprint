/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.TcpSynData;

@ProxyFor(value = TcpSynData.class)
public interface TcpSynDataProxy extends ValueProxy, TcpSynDataCs {

}
