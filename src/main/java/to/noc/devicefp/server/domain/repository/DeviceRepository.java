/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import to.noc.devicefp.server.domain.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long>, DeviceRepositoryCustom {

    @Query(
        "select count(d.id) " +
        "from   Device d, ZombieCookie c " +
        "where  c.id = :cookieId " +
        // If the passed in cookie id is a subordinate cookie, we want to use
        // its parent id, otherwise its parent id is null and we use its id.
        "and    coalesce(c.parent.id, c.id) in (d.zombieCookie.id, d.zombieCookie.parent.id) " +
        "and    d.markedDeleted <> true "
    )
    long countLinkedDevices(@Param("cookieId") String cookieId);


    @Query(
        "select count(d.id) " +
        "from   Device d " +
        "join   d.zombieCookie.users u " +
        "where  u.id = :userId "    +
        "and    d.markedDeleted <> true "
    )
    long countUserDevices(@Param("userId") long userId);


    @Query(
        "select count(d.id) " +
        "from   Device d " +
        "where  d.ipAddress = :ipAddress "    +
        "and    d.markedDeleted <> true "
    )
    long countWithIpDevices(@Param("ipAddress") String ipAddress);

}
