/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.shared;

import to.noc.devicefp.shared.ValDisplayUtil;
import static org.junit.Assert.*;
import org.junit.Test;


public class ValDisplayUtilTest {

    /**
     * Test of getIntervalStr method, of class ValDisplayUtil.
     */
    @Test
    public void testGetIntervalStr_long() {
        long ms = 0;
        assertEquals("0 minutes, 0 seconds", ValDisplayUtil.getIntervalStr(ms));
        ms = 499; // round down ms
        assertEquals("0 minutes, 0 seconds", ValDisplayUtil.getIntervalStr(ms));
        ms++; // round up ms
        assertEquals("0 minutes, 1 second", ValDisplayUtil.getIntervalStr(ms));
        ms += 1000 * 60;
        assertEquals("1 minute, 1 second", ValDisplayUtil.getIntervalStr(ms));
        ms += 1000 * 60 * 2; // +2 minutes
        ms += 1000;          // +1 second
        assertEquals("3 minutes, 2 seconds", ValDisplayUtil.getIntervalStr(ms));
        ms += 1000 * 60 * 60;
        assertEquals("1 hour, 3 minutes, 2 seconds", ValDisplayUtil.getIntervalStr(ms));
        ms += 1000 * 60 * 60;
        assertEquals("2 hours, 3 minutes, 2 seconds", ValDisplayUtil.getIntervalStr(ms));
        ms += 1000 * 60 * 60 * 24; // +1 day
        assertEquals("1 day, 2:03:02", ValDisplayUtil.getIntervalStr(ms));
        ms += 1000 * 60 * 60 * 24 * 10; // +10 days
        ms += 1000 * 60 * 60 * 10;      // +10 hours
        ms += 1000 * 60 * 10;           // +10 minutes
        ms += 1000 * 10;                // +10 seconds
        assertEquals("11 days, 12:13:12", ValDisplayUtil.getIntervalStr(ms));
    }

}
