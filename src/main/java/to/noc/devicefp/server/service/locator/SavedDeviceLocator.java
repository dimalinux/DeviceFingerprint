/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.Service;
import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.service.SavedDeviceService;

@Service(value=SavedDeviceService.class) /* not a spring annotation! */
public class SavedDeviceLocator extends Locator<Device, Long> {

    @Override
    public Device create(Class<? extends Device> clazz) {
        throw new UnsupportedOperationException("Clients don't create saved devices");
    }

    @Override
    public Device find(Class<? extends Device> clazz, Long id) {
        // After orriding isLive, this is never called by the RequestFactoryServlet
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<Device> getDomainType() {
        return Device.class;
    }

    @Override
    public Long getId(Device device) {
        return device.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return Long.class;
    }

    @Override
    public Object getVersion(Device device) {
        return device.getVersion();
    }

    @Override
    public boolean isLive(Device device) {
        return !device.isMarkedDeleted();
    }
}
