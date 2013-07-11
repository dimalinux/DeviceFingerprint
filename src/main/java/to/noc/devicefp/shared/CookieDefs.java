/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.shared;

/**
 *   Values needed on both server and GWT client
 */
public class CookieDefs {
    public static final String DEVICE_COOKIE_NAME = "device";
    public static final int DEVICE_COOKIE_MAX_AGE_SECONDS = 60 * 60 * 24 * 365 * 10; // roughly 10 years
    public static final long DEVICE_COOKIE_MAX_AGE_MS = DEVICE_COOKIE_MAX_AGE_SECONDS * 1000L;
}
