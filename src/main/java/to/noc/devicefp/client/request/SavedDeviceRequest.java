/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.request;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import java.util.List;
import to.noc.devicefp.client.entity.DeviceProxy;
import to.noc.devicefp.server.service.SavedDeviceService;
import to.noc.devicefp.server.service.locator.SpringServiceLocator;

@Service(value = SavedDeviceService.class, locator = SpringServiceLocator.class)
public interface SavedDeviceRequest extends RequestContext {

    Request<Long> countSaved();
    Request<List<DeviceProxy>> findSaved(int firstResult, int maxResults);

    Request<Long> countWithSameIp();
    Request<List<DeviceProxy>> findWithSameIp(int firstResult, int maxResults);

    Request<Long> countAllOther();
    Request<List<DeviceProxy>> findAllOther(int firstResult, int maxResults);

    Request<Void> markDeleted(Long deviceId);
}
