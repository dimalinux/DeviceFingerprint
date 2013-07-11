/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import to.noc.devicefp.server.domain.entity.MaxMindLocation;

public class MaxMindLocationRepositoryImpl implements MaxMindLocationRepositoryCustom {
    private static final Logger log = LoggerFactory.getLogger(MaxMindLocationRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly=true)
     public MaxMindLocation getLatestAfterDate(String ipAddress, Date afterTimestamp) {
        Query query = entityManager.createQuery(
            "select loc " +
            "from   MaxMindLocation loc " +
            "where  loc.ipAddress = :ipAddress " +
            "and    loc.stamp > :afterTimestamp " +
            "order by loc.stamp desc " +
            ")"
        );

        query.setParameter("ipAddress", ipAddress);
        query.setParameter("afterTimestamp", afterTimestamp);
        query.setMaxResults(1);

        MaxMindLocation result = null;
        try {
            result = (MaxMindLocation) query.getSingleResult();
        } catch(NoResultException|EmptyResultDataAccessException ex) {
            log.debug("{}: getLatestAfterDate(ipAddress={}, afterTimestamp={})",
                    ex.getClass().getSimpleName(), afterTimestamp);
        }

        return result;
    }
}
