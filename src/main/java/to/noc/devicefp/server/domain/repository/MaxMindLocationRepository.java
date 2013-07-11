/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import to.noc.devicefp.server.domain.entity.MaxMindLocation;

public interface MaxMindLocationRepository extends JpaRepository<MaxMindLocation, Long>, MaxMindLocationRepositoryCustom {

}
