/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import com.mysema.query.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.domain.entity.OpenIdUser;
import to.noc.devicefp.server.domain.entity.QDevice;
import static to.noc.devicefp.server.domain.repository.DeviceSpecifier.*;
import to.noc.devicefp.server.util.DeviceSearchString;

public class DeviceRepositoryImpl implements DeviceRepositoryCustom {
    private static final Logger log = LoggerFactory.getLogger(DeviceRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * Returns devices linked to the passed in cookie (and not marked as
     * deleted).
     */
    @Override
    @Transactional(readOnly=true)
    @SuppressWarnings("unchecked")
    public List<Device> findLinkedDevices(String cookieId, int firstResult, int maxResults) {
        Query query = entityManager.createQuery(
            "select d " +
            "from   Device d, ZombieCookie c " +
            "where  c.id = :cookieId " +
            // If the passed in cookie id is a subordinate cookie, we want to use its parent id,
            // otherwise its parent id is null and we use its id.
            "and    coalesce(c.parent.id, c.id) in (d.zombieCookie.id, d.zombieCookie.parent.id) " +
            "and    d.markedDeleted <> true " +
            "order by " +
            "       d.id desc "
        );
        query.setParameter("cookieId", cookieId);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);

        return query.getResultList();
    }


    /*
     * Returns the device with the passed in deviceId if the following
     * criteria are met:
     *   1) The device exists
     *   2) It is not marked as deleted
     *   3) It's cookie is linked to the passed in cookie
     */
    @Override
    @Transactional(readOnly=true)
    public Device findLinkedDevice(String cookieId, long deviceId) {
        Query query = entityManager.createQuery(
            "select d " +
            "from   Device d, ZombieCookie c " +
            "where  d.id = :deviceId " +
            "and    c.id = :cookieId " +
            "and    coalesce(c.parent.id, c.id) in (d.zombieCookie.id, d.zombieCookie.parent.id) " +
            "and    d.markedDeleted <> true "
        );
        query.setParameter("cookieId", cookieId);
        query.setParameter("deviceId", deviceId);

        Device result = null;

        try {
            result = (Device) query.getSingleResult();
        } catch(NoResultException|EmptyResultDataAccessException ex) {
            log.warn("{}: findLinkedDevice(cookieId={}, deviceId={})",
                    ex.getClass().getSimpleName(), cookieId, deviceId);
        }

        return result;
    }


    /*
     * Returns user devices not marked as deleted.
     */
    @Override
    @Transactional(readOnly=true)
    @SuppressWarnings("unchecked")
    public List<Device> findUserDevices(long userId, int firstResult, int maxResults) {
        Query query = entityManager.createQuery(
            "select d " +
            "from   Device d " +
            "join   d.zombieCookie.users u " +
            "where  u.id = :userId "    +
            "and    d.markedDeleted <> true " +
            "order by " +
            "       d.id desc "
        );
        query.setParameter("userId", userId);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);

        return query.getResultList();
    }


    /*
     * Returns the requested device if it both exists and is owned by the passed in
     * user id.
     */
    @Override
    @Transactional(readOnly=true)
    public Device findUserDevice(long userId, long deviceId) {
        Query query = entityManager.createQuery(
            "select d " +
            "from   Device d " +
            "join   d.zombieCookie.users u " +
            "where  u.id = :userId " +
            "and    d.id = :deviceId " +
            "and    d.markedDeleted <> true "

        );
        query.setParameter("userId", userId);
        query.setParameter("deviceId", deviceId);

        Device result = null;

        try {
            result = (Device) query.getSingleResult();
        } catch(NoResultException|EmptyResultDataAccessException ex) {
            log.warn("{}: findUserDevice(userId={}, deviceId={})",
                    ex.getClass().getSimpleName(), userId, deviceId);
        }

        return result;
    }


    /*
     * Returns devices with the passed in source IpAddress.
     */
    @Override
    @Transactional(readOnly=true)
    @SuppressWarnings("unchecked")
    public List<Device> findWithIpDevices(String ipAddress, int firstResult, int maxResults) {
        Query query = entityManager.createQuery(
            "select d " +
            "from   Device d " +
            "where  d.ipAddress = :ipAddress " +
            "and    d.markedDeleted <> true " +
            "order by " +
            "       d.id desc "
        );
        query.setParameter("ipAddress", ipAddress);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);

        return query.getResultList();
    }


    /*
     * Returns the device with the passed in deviceId if it exists and also
     * has the passed in source ip address.
     */
    @Override
    @Transactional(readOnly=true)
    public Device findWithIpDevice(String ipAddress, long deviceId) {
        Query query = entityManager.createQuery(
            "select d " +
            "from   Device d " +
            "where  d.id = :deviceId " +
            "and    d.ipAddress = :ipAddress " +
            "and    d.markedDeleted <> true "
        );
        query.setParameter("ipAddress", ipAddress);
        query.setParameter("deviceId", deviceId);

        Device result = null;

        try {
            result = (Device) query.getSingleResult();
        } catch(NoResultException|EmptyResultDataAccessException ex) {
            log.warn("{}: findWithIpDevice(ipAddress={}, deviceId={})",
                    ex.getClass().getSimpleName(), ipAddress, deviceId);
        }

        return result;
    }


    @Override
    @Transactional(readOnly=true)
    public long countAdminView(OpenIdUser currentUser, String search) {

        return createAdminViewQuery(currentUser, search)
                .count();
    }

    @Override
    @Transactional(readOnly=true)
    public List<Device> findAdminView(OpenIdUser currentUser, String search, int firstResult, int maxResults) {

        return createAdminViewQuery(currentUser, search)
                .orderBy(newestToOldest())
                .offset(firstResult)
                .limit(maxResults)
                .list(QDevice.device);
    }


    public JPAQuery createAdminViewQuery(OpenIdUser user, String search) {

        DeviceSearchString dss = new DeviceSearchString(search != null ? search : "");
        QDevice qDevice = QDevice.device;
        JPAQuery query = new JPAQuery(entityManager).from(qDevice);

        if (dss.hasJs()) {
            query = query.innerJoin(qDevice.jsData);
        }

        Long deviceId = dss.parseDeviceId();
        if (deviceId != null) {
            query = query.where(qDevice.id.eq(deviceId));
        }

        //  Use the email being searched on or, by default, skip over imprints
        //  owned by the current admin user.
        String email = dss.parseEmail();
        if (email != null) {
            query = query.where(hasEmail(email));
        } else {
            query = query.where(isNotUser(user));
        }

        String ip = dss.parseIpAddress();
        if (ip != null) {
            query = query.where(hasIp(ip));
        }

        String cookieId = dss.parseCookieId();
        if (cookieId != null) {
            query = query.where(hasExactCookieId(cookieId));
        }

        return query;
    }
}
