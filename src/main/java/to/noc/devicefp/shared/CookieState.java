/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.shared;


/*
 *   NEW:       Cookie had no previous value and its current value is brand new
 *              (i.e. current value was created during the page load).
 *
 *   EXISTING:  Cookie already existed with the current best value.
 *
 *   RESET:     Any cookie that has a bad value, will get the state "RESET".
 *              This typically only occurs when someone is testing the
 *              site by modifying their cookie values.  The RESET state takes
 *              priority over NEW or the RESTORED_FROM_* states.
 *
 *   LINKED_BUT_NOT_SYNCED:
 *              ETag cookies can only be set during the intial page response.
 *              If an ETag value is superceeded by a client storage cookie (Webstorage,
 *              Flash or Silverlight), we use this state. The ETag's value
 *              will be synched on the next page load.  Note that the superceeded
 *              state can either NEW or EXISTING.
 *
 *   RESTORED FROM ...:
 *              Cookie had no previous value.  The newly assigned value was taken
 *              from a different cookie type that already existed.
 *
 *   REPLACED BY ...:
 *              Cookie had a valid value, but was superceeded by an older cookie.
 *              Different cookies assigned to the same client are linked in our
 *              database.  Because of this, it's possible for a cookie with an
 *              older parent to be superceeded by it's own cookie type.
 */

public enum CookieState {
    NOT_SUPPORTED,
    NEW,
    EXISTING,
    LINKED_BUT_NOT_SYNCED,
    RESET,

    RESTORED_FROM_PLAIN_COOKIE,
    RESTORED_FROM_ETAG_COOKIE,
    RESTORED_FROM_WEB_STORAGE_COOKIE,
    RESTORED_FROM_FLASH_COOKIE,
    RESTORED_FROM_SILVERLIGHT_COOKIE,

    REPLACED_BY_OLDER_PLAIN_COOKIE,
    REPLACED_BY_OLDER_ETAG_COOKIE,
    REPLACED_BY_OLDER_WEB_STORAGE_COOKIE,
    REPLACED_BY_OLDER_FLASH_COOKIE,
    REPLACED_BY_OLDER_SILVERLIGHT_COOKIE;

    public boolean isReplacedByOlderState() {
        switch (this) {
            case REPLACED_BY_OLDER_PLAIN_COOKIE:
            case REPLACED_BY_OLDER_ETAG_COOKIE:
            case REPLACED_BY_OLDER_WEB_STORAGE_COOKIE:
            case REPLACED_BY_OLDER_FLASH_COOKIE:
            case REPLACED_BY_OLDER_SILVERLIGHT_COOKIE:
                return true;
            default:
                return false;
        }
    }
}
