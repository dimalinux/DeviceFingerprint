/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.util;

import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Test;
import static to.noc.devicefp.server.util.ETagUtil.*;

public class ETagUtilTest {

    @Test
    public void test_createEtag() {
        assertEquals("\"XYZ/1970-01-01-00:00:00.000\"", createEtag("XYZ", new Date(0)));
    }

    @Test
    public void test_parseEtagCookieId() {
        assertEquals("A-B-C-D", parseEtagCookieId("\"A-B-C-D/1970-01-01-00:00:00.000\""));
    }

    @Test
    public void test_parseEtagCookieId_verifyNull() {
        assertNull(parseEtagCookieId(""));
        assertNull(parseEtagCookieId(null));
    }

    @Test
    public void test_parseEtagDate() {
        Date epoch = new Date(0); // start of epoch
        String etag = "\"aaa-bbb-ccc-ddd/1970-01-01-00:00:00.000\"";
        assertEquals(epoch, parseEtagDate(etag));
    }

    @Test
    public void test_parseEtagDate_noDate() {
        assertNull(parseEtagDate("\"aaa-bbb-ccc-ddd/\""));
        assertNull(parseEtagDate(""));
        assertNull(null);
    }

    @Test
    public void test_parseEtagDate_badDate() {
        // value longer than Long.MAX_VALUE
        assertNull(parseEtagDate("\"abc_99999999999999999999\""));
    }

    @Test
    public void test_roundTrip() {
        Date now = new Date();
        String id = "39b2ff5c-9894-4f12-a86b-956b1e75adc4";
        String etag = createEtag(id, now);
        assertEquals(id, parseEtagCookieId(etag));
        assertEquals(now, parseEtagDate(etag));
     }
}
