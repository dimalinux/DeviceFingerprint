/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import java.util.List;
import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.domain.entity.OpenIdUser;

public interface DeviceRepositoryCustom {
    public Device findLinkedDevice(String cookieId, long deviceId);
    public List<Device> findLinkedDevices(String cookieId, int firstResult, int maxResults);

    public Device findUserDevice(long userId, long deviceId);
    public List<Device> findUserDevices(long userId, int firstResult, int maxResults);

    public Device findWithIpDevice(String ipAddress, long deviceId);
    public List<Device> findWithIpDevices(String ipAddress, int firstResult, int maxResults);

    long countAdminView(OpenIdUser user, String searchNarrow);
    public List<Device> findAdminView(OpenIdUser user, String searchNarrow, int firstResult, int maxResults);

}
