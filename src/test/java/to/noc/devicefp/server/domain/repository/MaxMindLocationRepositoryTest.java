/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import to.noc.devicefp.server.config.InMemoryJpaConfig;
import to.noc.devicefp.server.config.PropertiesConfig;
import to.noc.devicefp.server.domain.entity.MaxMindLocation;
import static to.noc.devicefp.server.domain.entity.MaxMindLocation.createMockLocation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PropertiesConfig.class, InMemoryJpaConfig.class})
@Transactional
public class MaxMindLocationRepositoryTest {
    
    // queries use relative times, so these can all be static
    private static final long EPOC_MS = System.currentTimeMillis();
    private static final long HOUR_MS = 1000 * 60 * 60;
    private static final Date HOUR_AGO = new Date(EPOC_MS - HOUR_MS);
    private static final Date TWO_HOURS_AGO = new Date(EPOC_MS - 2 * HOUR_MS);
    private static final Date DAY_AGO = new Date(EPOC_MS - 24 * HOUR_MS);

    @Autowired
    private MaxMindLocationRepository repository;

    @Test
    public void test_getLatestAfterDate() {
        String ipAddress = "10.0.250.1";

        MaxMindLocation location =
                repository.save(createMockLocation(ipAddress, HOUR_AGO));

        MaxMindLocation result =
                repository.getLatestAfterDate(ipAddress, DAY_AGO);

        assertEquals(location, result);
    }

    @Test
    public void testFindExistingWithMultipleEntries() {
        String ipAddress = "192.168.250.1";

        repository.save(createMockLocation(ipAddress, TWO_HOURS_AGO));
        MaxMindLocation latestLocation =
                repository.save(createMockLocation(ipAddress, HOUR_AGO));

        MaxMindLocation result =
                repository.getLatestAfterDate(ipAddress, DAY_AGO);

        assertEquals(latestLocation, result);
    }

    @Test
    public void testNoRecentEntry() {
        String ipAddress = "10.0.250.1";

        repository.save(createMockLocation(ipAddress, DAY_AGO));

        MaxMindLocation result =
                repository.getLatestAfterDate(ipAddress, TWO_HOURS_AGO);

        assertEquals(null, result);
    }
}
