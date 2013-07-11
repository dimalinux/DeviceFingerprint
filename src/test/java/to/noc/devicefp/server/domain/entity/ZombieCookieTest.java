/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class ZombieCookieTest {

    private ZombieCookie oldest;
    private ZombieCookie middle;
    private ZombieCookie youngest;

    private OpenIdUser user1;
    private OpenIdUser user2;

    @Before
    public void initialize() {
        long nowMs = System.currentTimeMillis();
        long dayMs = 24 * 60 * 60 * 1000;

        oldest = new ZombieCookie(new Date(nowMs - 100 * dayMs));
        middle = new ZombieCookie(new Date(nowMs - 10 * dayMs));
        youngest = new ZombieCookie(new Date(nowMs));

        oldest.setId("oldest"); // non-random ids are easier to debug
        middle.setId("middle");
        youngest.setId("youngest");

        user1 = new OpenIdUser();
        user2 = new OpenIdUser();
        user1.setId(1L);
        user2.setId(2L);
    }

    @Test
    public void test_isOlder() {
        assertTrue(oldest.isOlder(middle));
        assertFalse(middle.isOlder(oldest));
        assertFalse(youngest.isOlder(youngest));
    }

    @Test
    public void test_linkToCookieResult() {
        assertEquals(middle.linkTo(youngest), middle); // older to younger
        assertEquals(youngest.linkTo(oldest), oldest); // younger to older
    }

    @Test
    public void test_isLinkedToCookie() {
        assertFalse(middle.isLinkedTo(youngest));
        youngest.linkTo(middle);
        assertTrue(middle.isLinkedTo(youngest));
        assertTrue(youngest.isLinkedTo(middle));
    }

    @Test
    public void test_getParent() {
        youngest.linkTo(middle).linkTo(oldest);
        assertNull(oldest.getParent());
        assertEquals(youngest.getParent(), oldest); // flat hierarchy
        assertEquals(middle.getParent(), oldest);

    }

    @Test
    public void test_isLinkedToUser() {
        middle.linkTo(user1);
        assertTrue(middle.isLinkedTo(user1));
        assertFalse(middle.isLinkedTo(user2));
    }


    @Test
    public void test_userPropagateUp() {
        middle.linkTo(youngest);
        youngest.linkTo(user1);
        assertTrue(middle.isLinkedTo(user1));
    }

    @Test
    public void test_userPropagateDown() {
        oldest.linkTo(middle).linkTo(youngest);
        oldest.linkTo(user1);
        assertTrue(middle.isLinkedTo(user1));
        assertTrue(youngest.isLinkedTo(user1));
    }

    @Test
    public void test_userCrossPropagation() {
        middle.linkTo(user1);
        oldest.linkTo(user2);

        middle.linkTo(oldest);  // users should cross propagate when cookies linked

        assertTrue(middle.isLinkedTo(user1));
        assertTrue(middle.isLinkedTo(user2));

        assertTrue(oldest.isLinkedTo(user1));
        assertTrue(oldest.isLinkedTo(user2));
    }

}
