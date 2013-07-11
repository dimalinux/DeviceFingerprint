/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import to.noc.devicefp.server.domain.entity.CookieStates;
import to.noc.devicefp.server.domain.entity.FlashData;
import to.noc.devicefp.server.domain.entity.JsData;
import to.noc.devicefp.server.domain.entity.SilverlightData;
import to.noc.devicefp.server.domain.entity.ZombieCookie;
import to.noc.devicefp.shared.CookieState;
import static to.noc.devicefp.shared.CookieState.*;
import static to.noc.devicefp.shared.CookieType.*;

public class AbstractCookieStatesUpdaterTest {

    private AbstractCookieStatesUpdater cookieStatesUpdater;
    private CookieStates cookieStates;
    private static final int ONE_DAY_MS = 24 * 60 * 60 * 1000;
    private static final Date inceptionDate = new Date(System.currentTimeMillis() - ONE_DAY_MS);
    private static final Date olderInceptionDate = new Date(inceptionDate.getTime() - ONE_DAY_MS);

    @Before
    public void initialize() {
        cookieStates = new CookieStates();

        cookieStatesUpdater = new AbstractCookieStatesUpdater() {
            private int newCookies = 0;
            ZombieCookie cookie = null;

            @Override
            protected CookieStates getCookieStates() {
                return cookieStates;
            }

            @Override
            protected ZombieCookie createZombieCookie() {
                ZombieCookie newCookie = new ZombieCookie();
                newCookie.setId("random_" + newCookies++);
                newCookie.setInception(new Date());
                return newCookie;
            }

            @Override
            protected ZombieCookie getZombieCookie() {
                return cookie;
            }

            @Override
            protected ZombieCookie setAndSaveZombieCookie(ZombieCookie cookie) {
                this.cookie = cookie;
                return cookie;
            }

            @Override
            protected ZombieCookie lookupZombieCookie(String id) {
                ZombieCookie found = null;
                // Test IDs must start either with "existing" or with "not_in_db".
                // A cookie that starts with "existing_older" gets an inception
                // date that is older than a cookie that just starts with
                // "existing".
                if (id.startsWith("existing")) {
                    found = new ZombieCookie();
                    found.setId(id);
                    Date inception = id.startsWith("existing_older") ? olderInceptionDate : inceptionDate;
                    found.setInception(inception);
                } else {
                    assertTrue(id.startsWith("not_in_db"));
                }
                // If the cookie name ends with "has_parent", attach an older parent
                // cookie.
                if (found != null && id.endsWith("has_parent")) {
                    ZombieCookie parent = new ZombieCookie();
                    parent.setId(id.replace("has_parent", "parent"));
                    parent.setInception(new Date(found.getInception().getTime() - 10000));
                    found.linkTo(parent);
                }
                return found;
            }
        };

    }

    private void setIntermediateStates(
            String id,
            CookieState plain,
            CookieState etag,
            CookieState web,
            CookieState flash,
            CookieState silverlight
            ) {
        ZombieCookie zombieCookie = cookieStatesUpdater.lookupZombieCookie(id);
        cookieStatesUpdater.setAndSaveZombieCookie(zombieCookie);
        cookieStates.setPlainState(plain);
        cookieStates.setEtagState(etag);
        cookieStates.setWebStorageState(web);
        cookieStates.setFlashState(flash);
        cookieStates.setSilverlightState(silverlight);
    }

    private void assertStates(
            String id,
            CookieState plain,
            CookieState etag,
            CookieState web,
            CookieState flash,
            CookieState silverlight
            ) {
        assertEquals(id, cookieStatesUpdater.getZombieCookie().getId());
        assertEquals(plain, cookieStates.getPlainState());
        assertEquals(etag, cookieStates.getEtagState());
        assertEquals(web, cookieStates.getWebStorageState());
        assertEquals(flash, cookieStates.getFlashState());
        assertEquals(silverlight, cookieStates.getSilverlightState());
    }

    /***********************************************************************/

    @Test
    public void test_NoPlain_NoEtag() {
        cookieStatesUpdater.initCookieStates(null, null);
        // plain cookie and etag set with new value on home page response
        assertStates("random_0", NEW, NEW, null, null, null);
    }

    @Test
    public void test_NoPlain_YesEtag() {
        cookieStatesUpdater.initCookieStates(null, "existing_etag");
        assertStates("existing_etag", RESTORED_FROM_ETAG_COOKIE, EXISTING, null, null, null);
    }

    @Test
    public void test_YesPlain_NoEtag() {
        cookieStatesUpdater.initCookieStates("existing_plain", null);
        assertStates("existing_plain", EXISTING, RESTORED_FROM_PLAIN_COOKIE, null, null, null);
    }

    @Test
    public void test_YesPlain_YesEtag_Identical() {
        cookieStatesUpdater.initCookieStates("existing", "existing");
        assertStates("existing", EXISTING, EXISTING, null, null, null);
    }

    @Test
    public void test_YesPlain_YesEtag_DifferButSameInception() {
        cookieStatesUpdater.initCookieStates("existing_plain", "existing_etag");
        //  We now have two valid but different cookies with the same inception
        //  date.  This can't happen in practice, but the code shouldn't break.
        //  Both "existing_plain" and "existing_etag" are valid answers, but our
        //  algorithm should pick the plain cookie, as its enumerated value is
        //  delcared earlier in the CookieType enumeration.
        assertStates("existing_plain", EXISTING, REPLACED_BY_OLDER_PLAIN_COOKIE, null, null, null);
    }

    /***********************************************************************/

    @Test
    public void test_PlainOlderEtag() {
        cookieStatesUpdater.initCookieStates("existing_older_plain", "existing_etag");
        assertStates("existing_older_plain", EXISTING, REPLACED_BY_OLDER_PLAIN_COOKIE, null, null, null);
    }

    @Test
    public void test_EtagOlderPlain() {
        cookieStatesUpdater.initCookieStates("existing_plain", "existing_older_etag");
        assertStates("existing_older_etag", REPLACED_BY_OLDER_ETAG_COOKIE, EXISTING, null, null, null);
    }

    /***********************************************************************/

    @Test
    public void test_BadPlain_BadEtag() {
        cookieStatesUpdater.initCookieStates("not_in_db_plain", "not_in_db_etag");
        assertStates("random_0", RESET, RESET, null, null, null);
    }

    @Test
    public void test_BadPlain_GoodEtag() {
        cookieStatesUpdater.initCookieStates("not_in_db_plain", "existing_etag");
        assertStates("existing_etag", RESET, EXISTING, null, null, null);
    }

    @Test
    public void test_GoodPlain_BadEtag() {
        cookieStatesUpdater.initCookieStates("existing_plain", "not_in_db_etag");
        assertStates("existing_plain", EXISTING, RESET, null, null, null);
    }

    // Already tested above: testGoodPlainCookie_GoodEtag()

    /***********************************************************************/

    @Test
    public void test_BadPlainCookieNoEtag() {
        cookieStatesUpdater.initCookieStates("not_in_db_plain", null);
        assertStates("random_0", RESET, NEW, null, null, null);
    }

    @Test
    public void test_NoPlainCookieBadEtag() {
        cookieStatesUpdater.initCookieStates(null, "not_in_db_etag");
        assertStates("random_0", NEW, RESET, null, null, null);
    }

   /***********************************************************************/

    @Test
    public void test_NoPlain_EtagHasOlderParent() {
        cookieStatesUpdater.initCookieStates(null, "existing_etag_has_parent");
        assertStates("existing_etag_parent", RESTORED_FROM_ETAG_COOKIE, REPLACED_BY_OLDER_ETAG_COOKIE, null, null, null);
    }

    @Test
    public void test_YesPlain_EtagHasOldestParent() {
        cookieStatesUpdater.initCookieStates("existing", "existing_etag_has_parent");
        assertStates("existing_etag_parent", REPLACED_BY_OLDER_ETAG_COOKIE, REPLACED_BY_OLDER_ETAG_COOKIE, null, null, null);
    }

    @Test
    public void test_PlainHasOldestParent_NoEtag() {
        cookieStatesUpdater.initCookieStates("existing_plain_has_parent", null);
        assertStates("existing_plain_parent", REPLACED_BY_OLDER_PLAIN_COOKIE, RESTORED_FROM_PLAIN_COOKIE, null, null, null);
    }

    @Test
    public void test_PlainHasOldestParent_YesEtag() {
        cookieStatesUpdater.initCookieStates("existing_plain_has_parent", "existing");
        assertStates("existing_plain_parent", REPLACED_BY_OLDER_PLAIN_COOKIE, REPLACED_BY_OLDER_PLAIN_COOKIE, null, null, null);
    }


    /***********************************************************************/

    @Test
    public void test_PlainNotSupported_WebstorageSupported() {
        JsData jsData = new JsData();
        jsData.setWebLocalStorageTest(true);   // webstorage cookies supported
        boolean plainCookiesSupport = false;   // plain cookies not supported

        cookieStatesUpdater.initCookieStates(null, "not_in_db_etag");
        assertStates("random_0", NEW, RESET, null, null, null);
        cookieStatesUpdater.updateCookieStates(jsData, plainCookiesSupport);
        cookieStatesUpdater.updateCookieStates(WEB_STORAGE, null);

        assertStates(
                "random_0",
                NOT_SUPPORTED,                  // Plain
                RESET,                          // ETag
                NEW,                            // Webstorage
                null,                           // Flash
                null                            // Silverlight
                );

    }

    @Test
    public void test_PlainSupported_WebstorageNotSupported() {
        JsData jsData = new JsData();
        jsData.setWebLocalStorageTest(false);  // webstorage not supported
        boolean plainCookiesSupport = true;    // plain cookies supported

        cookieStatesUpdater.initCookieStates(null, null);
        assertStates("random_0", NEW, NEW, null, null, null);

        cookieStatesUpdater.updateCookieStates(jsData, plainCookiesSupport);
        cookieStatesUpdater.updateCookieStates(WEB_STORAGE, null);

        assertStates(
                "random_0",
                NEW,            // Plain
                NEW,            // ETag
                NOT_SUPPORTED,  // Webstorage
                null,           // Flash
                null            // Silverlight
                );

    }

    /***********************************************************************/

    @Test
    public void test_FlashCookiesNotSupported() {
        FlashData flashDataNoCookieSupport = new FlashData();
        flashDataNoCookieSupport.setLsoStorageTest(false); // flash cookies not supported

        setIntermediateStates("existing_by_intermediate_state_but_new", NEW, NEW, NEW, null, NEW);

        cookieStatesUpdater.updateCookieStates(flashDataNoCookieSupport);
        cookieStatesUpdater.updateCookieStates(FLASH, null);

        assertStates(
                "existing_by_intermediate_state_but_new",
                NEW,            // Plain
                NEW,            // ETag
                NEW,            // Webstorage
                NOT_SUPPORTED,  // Flash
                NEW             // Silverlight
                );

    }


    @Test
    public void test_FlashCookiesSupported() {
        FlashData flashDataWithCookieSupport = new FlashData();
        flashDataWithCookieSupport.setLsoStorageTest(true); // flash cookies supported

        setIntermediateStates(
                "existing",
                EXISTING,                   // Plain
                EXISTING,                   // ETag
                RESTORED_FROM_PLAIN_COOKIE, // Webstorage
                null,                       // Flash
                NOT_SUPPORTED               // Silverlight
                );

        cookieStatesUpdater.updateCookieStates(flashDataWithCookieSupport);
        cookieStatesUpdater.updateCookieStates(FLASH, "existing_older");

        assertStates(
                "existing_older",
                REPLACED_BY_OLDER_FLASH_COOKIE, // Plain
                LINKED_BUT_NOT_SYNCED,          // ETag
                RESTORED_FROM_FLASH_COOKIE,     // Webstorage
                EXISTING,                       // Flash
                NOT_SUPPORTED                   // Silverlight
                );

    }

    @Test
    public void test_FlashCookieSupportButBadValue() {
        FlashData flashDataWithCookieSupport = new FlashData();
        flashDataWithCookieSupport.setLsoStorageTest(true); // flash cookies supported

        setIntermediateStates("existing", EXISTING, EXISTING, EXISTING, null, RESTORED_FROM_PLAIN_COOKIE);
        cookieStatesUpdater.updateCookieStates(flashDataWithCookieSupport);
        cookieStatesUpdater.updateCookieStates(FLASH, "not_in_db_flash");
        assertStates("existing", EXISTING, EXISTING, EXISTING, RESET, RESTORED_FROM_PLAIN_COOKIE);

    }

    @Test
    public void test_SilverlightCookiesNotSupported() {
        SilverlightData silverlightDataNoCookies = new SilverlightData();
        silverlightDataNoCookies.setIsolatedStorageTest(false);  // silverlight cookies not supported

        setIntermediateStates("existing", EXISTING, EXISTING, EXISTING, null, null);
        cookieStatesUpdater.updateCookieStates(silverlightDataNoCookies);
        cookieStatesUpdater.updateCookieStates(SILVERLIGHT, null);
        assertStates("existing", EXISTING, EXISTING, EXISTING, null, NOT_SUPPORTED);
    }

    @Test
    public void test_SilverlightSupported() {
        SilverlightData silverlightDataWithCookieSupport = new SilverlightData();  // silverlight cookies supported
        silverlightDataWithCookieSupport.setIsolatedStorageTest(true);

        setIntermediateStates("existing_older", EXISTING, EXISTING, EXISTING, null, null);
        cookieStatesUpdater.updateCookieStates(silverlightDataWithCookieSupport);
        cookieStatesUpdater.updateCookieStates(SILVERLIGHT, "existing_silverlight");
        assertStates("existing_older", EXISTING, EXISTING, EXISTING, null, REPLACED_BY_OLDER_WEB_STORAGE_COOKIE);
    }

    /***********************************************************************/

    @Test
    public void test_WebstoragePropogation_ToNewEtag() {
        JsData jsData = new JsData();
        jsData.setWebLocalStorageTest(true);  // webstorage supported
        boolean plainCookieSupport = false;   // plain cookies not supported

        cookieStatesUpdater.initCookieStates(null, null);
        ZombieCookie newCookieCreatedForEtag = cookieStatesUpdater.getZombieCookie();

        cookieStatesUpdater.updateCookieStates(jsData, plainCookieSupport);
        cookieStatesUpdater.updateCookieStates(WEB_STORAGE, "existing");
        ZombieCookie existingBestCookie = cookieStatesUpdater.getZombieCookie();

        assertStates(
                "existing",
                NOT_SUPPORTED,          // Plain
                LINKED_BUT_NOT_SYNCED,  // ETag
                EXISTING,               // Webstorage
                null,                   // Flash
                null                    // Silverlight
        );
        assert(newCookieCreatedForEtag.getParent().equals(existingBestCookie));
    }

    @Test
    public void test_WebstoragePropogation_ToExistingEtag_Override() {
        setIntermediateStates(
                "existing_etag",
                NOT_SUPPORTED,          // Plain
                EXISTING,               // ETag
                null,                   // Webstorage
                null,                   // Flash
                null                    // Silverlight
        );
        ZombieCookie existingEtagCookie = cookieStatesUpdater.getZombieCookie();

        cookieStatesUpdater.updateCookieStates(WEB_STORAGE, "existing_older_webstorage");
        ZombieCookie existingWebstorageCookie = cookieStatesUpdater.getZombieCookie();

        assertStates(
                "existing_older_webstorage",
                NOT_SUPPORTED,          // Plain
                LINKED_BUT_NOT_SYNCED,  // ETag
                EXISTING,               // Webstorage
                null,                   // Flash
                null                    // Silverlight
        );
        assert(existingEtagCookie.getParent().equals(existingWebstorageCookie));
    }

    @Test
    public void test_WebstoragePropogation_ToResetEtag() {
        JsData jsData = new JsData();
        jsData.setWebLocalStorageTest(true); // webstorage supported
        boolean plainCookieSupport = false;  // plain cookies not supported

        cookieStatesUpdater.initCookieStates(null, "not_in_db_etag");
        ZombieCookie newCookieCreatedForEtag = cookieStatesUpdater.getZombieCookie();

        cookieStatesUpdater.updateCookieStates(jsData, plainCookieSupport);
        cookieStatesUpdater.updateCookieStates(WEB_STORAGE, "existing");
        ZombieCookie existingBestCookie = cookieStatesUpdater.getZombieCookie();

        assertStates(
                "existing",
                NOT_SUPPORTED,      // Plain
                RESET,              // ETag
                EXISTING,           // Webstorage
                null,               // Flash
                null                // Silverlight
        );
        assert(newCookieCreatedForEtag.getParent().equals(existingBestCookie));
    }

    @Test
    public void test_FlashPropogation() {
        setIntermediateStates(
                "existing_when_flash_cookie_received_but_otherwise_new",
                NEW,                            // Plain
                RESET,                          // ETag
                NEW,                            // Webstorage
                null,                           // Flash
                NOT_SUPPORTED                   // Silverlight
                );
        cookieStatesUpdater.updateCookieStates(FLASH, "existing_older_flash");
        assertStates(
                "existing_older_flash",
                RESTORED_FROM_FLASH_COOKIE,     // Plain
                RESET,                          // ETag
                RESTORED_FROM_FLASH_COOKIE,     // Webstorage
                EXISTING,                       // Flash
                NOT_SUPPORTED                   // Silverlight
                );
    }

    @Test
    public void test_SilverlightHasOlderParentPropogation() {
        setIntermediateStates(
                "existing",
                EXISTING,                       // Plain
                LINKED_BUT_NOT_SYNCED,          // ETag
                REPLACED_BY_OLDER_FLASH_COOKIE, // Webstorage
                NOT_SUPPORTED,                  // Flash
                null                            // Silverlight
                );
        cookieStatesUpdater.updateCookieStates(SILVERLIGHT, "existing_older_silver_has_parent");
        assertStates(
                "existing_older_silver_parent",
                REPLACED_BY_OLDER_SILVERLIGHT_COOKIE,   // Plain
                LINKED_BUT_NOT_SYNCED,                  // ETag
                REPLACED_BY_OLDER_SILVERLIGHT_COOKIE,   // Webstorage
                NOT_SUPPORTED,                          // Flash
                REPLACED_BY_OLDER_SILVERLIGHT_COOKIE    // Silverlight
                );
    }

}
