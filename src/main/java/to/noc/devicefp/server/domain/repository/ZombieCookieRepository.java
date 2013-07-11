/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import to.noc.devicefp.server.domain.entity.ZombieCookie;

public interface ZombieCookieRepository extends JpaRepository<ZombieCookie, String> {

}
