/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import java.util.Date;
import to.noc.devicefp.server.domain.entity.MaxMindLocation;

public interface MaxMindLocationRepositoryCustom {

    MaxMindLocation getLatestAfterDate(String ipAddress, Date afterTimestamp);

}
