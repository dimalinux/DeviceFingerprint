/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import to.noc.devicefp.server.domain.entity.MaxMindLocation;

public interface MaxMindService {
    
    MaxMindLocation loadLocation(String ip);

}
