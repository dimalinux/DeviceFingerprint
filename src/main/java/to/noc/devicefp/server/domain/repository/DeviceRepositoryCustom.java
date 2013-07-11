/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import java.util.List;
import to.noc.devicefp.server.domain.entity.Device;

public interface DeviceRepositoryCustom {
    public List<Device> findLinkedDevices(String cookieId, int firstResult, int maxResults);
    public List<Device> findUserDevices(long userId, int firstResult, int maxResults);
    public List<Device> findWithIpDevices(String ipAddress, int firstResult, int maxResults);
    public List<Device> findAllDevicesButMine(long notMineUserId, int firstResult, int maxResults);

    public Device findLinkedDevice(String cookieId, long deviceId);
    public Device findUserDevice(long userId, long deviceId);
    public Device findWithIpDevice(String ipAddress, long deviceId);
}
