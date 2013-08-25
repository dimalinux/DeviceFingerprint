/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import java.util.Date;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import to.noc.devicefp.server.config.InMemoryJpaConfig;
import to.noc.devicefp.server.config.PropertiesConfig;
import to.noc.devicefp.server.domain.entity.Device;
import to.noc.devicefp.server.domain.entity.JsData;
import to.noc.devicefp.server.domain.entity.OpenIdUser;
import to.noc.devicefp.server.domain.entity.ZombieCookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PropertiesConfig.class, InMemoryJpaConfig.class } )
@Transactional
public class DeviceRepositoryTest {

    @Autowired private DeviceRepository deviceRepository;
    @Autowired private OpenIdUserRepository userRepository;
    @Autowired private ZombieCookieRepository cookieRepository;
    private static final String defaultIpAddress = "192.168.3.10";

    @Before
    public void beforeEachTest() {
        // make sure tests are being run on a fresh database
        assertEquals(0, deviceRepository.count());
        assertEquals(0, cookieRepository.count());
        assertEquals(0, userRepository.count());
    }


    /*
     * Setup method:  Allocates and persists the requested number of devices.
     */
    private Device[] createDeviceArray(int size) {
        long nowMs = System.currentTimeMillis();
        long dayMs = 24 * 60 * 60 * 1000;
        Device[] devices = new Device[size];

        for (int i = 0; i < devices.length; i++) {
            // Give each cookie a unique date, a 1-day gap makes easier debugging.
            Date date = new Date(nowMs - i*dayMs);
            ZombieCookie cookie = cookieRepository.save(new ZombieCookie(date));
            Device device = new Device(date, defaultIpAddress);
            device.setZombieCookie(cookie);
            devices[i] = deviceRepository.save(device);
        }

        return devices;
    }


    /*
     *  Setup method: Returns an array of the id values in the list.
     */
    private static Device[] deviceSubset(Device[] devices, int... indexes) {
        Device[] subset = new Device[indexes.length];
        int pos = 0;
        for (int index : indexes) {
            subset[pos++] = devices[index];
        }
        return subset;
    }


    /*
     * Setup method
     */
    private OpenIdUser createTestUser() {
        OpenIdUser user = new OpenIdUser();
        user.setOpenId("testUser_" + UUID.randomUUID().toString());
        return userRepository.save(user);
    }


    @Test
    public void test_LinkedDeviceQueries() {

        Device[] devices = createDeviceArray(10);
        String[] cids = new String[devices.length];

        for (int i = 0; i < devices.length; i++) {
            cids[i] = devices[i].getZombieCookie().getId();
            // link the cookies of the first 5 devices
            if (i < 4) {
                devices[i+1].getZombieCookie().linkTo(devices[i].getZombieCookie());
            }
        }

        devices[2].setMarkedDeleted(true);

        Device[] linkedDevs = deviceSubset(devices, 4, 3, 1, 0); // first 5 (in reverse order), skipping devices[2]
        Device device1 = devices[1];
        Long device1Id = device1.getId();

        assertEquals(linkedDevs.length, deviceRepository.countLinkedDevices(cids[0]));  // 0 is a subordinate cookie
        assertEquals(linkedDevs.length, deviceRepository.countLinkedDevices(cids[4]));  // 4 is oldest (parent of group)
        assertArrayEquals(linkedDevs, deviceRepository.findLinkedDevices(cids[1], 0, 1000).toArray());
        assertArrayEquals(linkedDevs, deviceRepository.findLinkedDevices(cids[4], 0, 1000).toArray());
        assertNull(deviceRepository.findLinkedDevice(cids[0], devices[2].getId()));  // 2 is marked as deleted
        assertNull(deviceRepository.findLinkedDevice(cids[4], devices[9].getId()));  // 9 is not linked
        assertEquals(device1, deviceRepository.findLinkedDevice(cids[0], device1Id)); // sibling cookie
        assertEquals(device1, deviceRepository.findLinkedDevice(cids[1], device1Id)); // same cookie as device
        assertEquals(device1, deviceRepository.findLinkedDevice(cids[4], device1Id)); // root cookie
    }


    @Test
    public void test_UserDeviceQueries() {
        Device[] devices = createDeviceArray(10);
        OpenIdUser user = createTestUser();

        devices[2].getZombieCookie().linkTo(user);

        // Link the first five cookies.  User will be propogated back to
        // devices 0,1 when device[2] is linked, and forward to devices 3,4.
        for(int i = 0; i < 4; i++) {
           devices[i].getZombieCookie().linkTo(devices[i+1].getZombieCookie());
        }

        devices[2].setMarkedDeleted(true);

        Device[] userDevices = deviceSubset(devices, 4, 3, 1, 0);

        assertEquals(userDevices.length, deviceRepository.countUserDevices(user.getId()));
        assertArrayEquals(userDevices, deviceRepository.findUserDevices(user.getId(), 0, 1000).toArray());

        // test single device calls
        assertNull(deviceRepository.findUserDevice(user.getId(), devices[9].getId()));
        assertEquals(devices[0], deviceRepository.findUserDevice(user.getId(), devices[0].getId()));
    }


    @Test
    public void test_WithIpQueries() {
        Device[] devices = createDeviceArray(10);

        // 10 devices, first two should not be included in results
        devices[0].setIpAddress("10.0.0.1");
        devices[1].setMarkedDeleted(true);

        Device[] linkedDevs = deviceSubset(devices, 9, 8, 7, 6, 5, 4, 3, 2);
        assertEquals(linkedDevs.length, deviceRepository.countWithIpDevices(defaultIpAddress));
        assertArrayEquals(linkedDevs, deviceRepository.findWithIpDevices(defaultIpAddress, 0, 1000).toArray());

        assertNull(deviceRepository.findWithIpDevice(defaultIpAddress, devices[0].getId()));
        assertNull(deviceRepository.findWithIpDevice(defaultIpAddress, devices[1].getId()));
        assertEquals(devices[2], deviceRepository.findWithIpDevice(defaultIpAddress, devices[2].getId()));
    }


    @Test
    public void test_AdminViewDevices_excludeCurrentUserFromResults() {
        String search = ""; // if no email is specified, current user is excluded
        Device[] devices = createDeviceArray(10);
        OpenIdUser user1 = createTestUser();
        OpenIdUser user2 = createTestUser();

        for(int i = 2; i < 10; i++) { // 0 and 1 not owned by either user
            if (i >= 7) {
                devices[i].getZombieCookie().linkTo(user1);
            }
            devices[i].getZombieCookie().linkTo(user2);
        }
        // 0,1,2,3,4,5,6,x,x,x:  not owned by user 1
        // 0,1,x,x,x,x,x,x,x,x:  not owned by user 2
        devices[2].isMarkedDeleted(); // these admin queries should include deleted devices

        Device[] notUser1 = deviceSubset(devices, 6, 5, 4, 3, 2, 1, 0);  // ordered by last index first
        Device[] notUser2 = deviceSubset(devices, 1, 0);

        assertEquals(notUser1.length, deviceRepository.countAdminView(user1, search));
        assertEquals(notUser2.length, deviceRepository.countAdminView(user2, search));

        assertArrayEquals(notUser1, deviceRepository.findAdminView(user1, search, 0, 1000).toArray());
        assertArrayEquals(notUser2, deviceRepository.findAdminView(user2, search, 0, 1000).toArray());
    }

    @Test
    public void test_AdminViewDevices_hasJavaScript() {
        String search = "hasJs";
        Device[] devices = createDeviceArray(10);
        OpenIdUser user = createTestUser();

        for(int i = 0; i < 5; i++) {
            devices[i].setJsData(new JsData());
            devices[i] = deviceRepository.save(devices[i]);
        }

        Device[] hasJsDevs = deviceSubset(devices, 4, 3, 2, 1, 0);

        assertEquals(hasJsDevs.length, deviceRepository.countAdminView(user, search));
        assertArrayEquals(hasJsDevs, deviceRepository.findAdminView(user, search, 0, 1000).toArray());
    }

}
