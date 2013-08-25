/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import java.util.List;
import to.noc.devicefp.server.domain.entity.Device;


public interface SavedDeviceService {

    long countSaved();
    List<Device> findSaved(int firstResult, int maxResults);

    long countWithSameIp();
    List<Device> findWithSameIp(int firstResult, int maxResults);

    long countAdminView(String search);
    List<Device> findAdminView(String search, int firstResult, int maxResults);

    void markDeleted(Long targetDeviceId);

}
