/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.request;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import java.util.Date;
import to.noc.devicefp.server.service.CurrentDeviceService;
import to.noc.devicefp.server.service.locator.SpringServiceLocator;

@Service(value = CurrentDeviceService.class, locator = SpringServiceLocator.class)
public interface SessionRequest extends RequestContext {
    Request<Void> windowClosed(Date windowCloseTimestamp);
    Request<Boolean> keepSessionAlive();
}
