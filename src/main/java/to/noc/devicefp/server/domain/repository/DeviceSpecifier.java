/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.repository;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import to.noc.devicefp.server.domain.entity.*;

public class DeviceSpecifier {
    private static final QDevice qDevice = QDevice.device;

    public static Predicate hasIp(final String ipAddress) {
        return qDevice.ipAddress.eq(ipAddress);
    }

    public static Predicate isNotDeleted() {
        return qDevice.markedDeleted.ne(Boolean.TRUE);
    }

    public static Predicate isNotUser(OpenIdUser user) {
        return qDevice.zombieCookie.users.contains(user).not();
    }

    public static Predicate hasEmail(String email) {
        return qDevice.zombieCookie.users.any().email.eq(email);
    }

    public static Predicate hasJavascript() {
        // QueryDsl must have bug with mapped super classes.  If we
        // just say jsData.isNotNull, QueryDsl checks
        return qDevice.jsData.userAgent.isNotNull();
    }

    public static Predicate hasExactCookieId(String cookieId) {
        return qDevice.zombieCookie.id.eq(cookieId);
    }

    public static OrderSpecifier newestToOldest() {
        return QDevice.device.id.desc();
    }

}
