/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import static java.lang.Math.abs;
import java.util.Date;

public class DateUtil {

    /**
     * Returns a string in the format "[+|-]hh:mm" for the GMT/UTC offset.
     */
    private static String formatUtcOffset(int utcOffSetMinutes) {

        if (utcOffSetMinutes == 0)
            return "+00:00";

        String sign = (utcOffSetMinutes < 0) ? "-" : "+";
        int min = abs(utcOffSetMinutes);

        String hh = twoDigitFmt(min / 60);
        String mm = twoDigitFmt(min % 60);

        return sign + hh + ":" + mm;
    }


    private static String twoDigitFmt(int i) {
        return (i < 10) ? ("0" + i) : Integer.toString(i);
    }

    @SuppressWarnings("deprecation")
    public static String formatTime(Date time, int savedUtcOffsetMinutes) {
        long adjustedMs = time.getTime() + (savedUtcOffsetMinutes * 60 * 1000);
        return new Date(adjustedMs).toGMTString().replace("GMT", "UTC") + formatUtcOffset(savedUtcOffsetMinutes);
    }
}
