/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import to.noc.devicefp.server.domain.entity.*;
import to.noc.devicefp.shared.CookieState;
import static to.noc.devicefp.shared.CookieState.*;
import to.noc.devicefp.shared.CookieType;
import static to.noc.devicefp.shared.CookieType.*;

public abstract class AbstractCookieStatesUpdater {

    private static final Logger log = LoggerFactory.getLogger(AbstractCookieStatesUpdater.class);

    protected abstract CookieStates getCookieStates();
    protected abstract ZombieCookie createZombieCookie();
    protected abstract ZombieCookie getZombieCookie();
    protected abstract ZombieCookie setAndSaveZombieCookie(ZombieCookie cookie);
    protected abstract ZombieCookie lookupZombieCookie(String id);

    /*
     * Takes the initial client cookie value, if any (null otherwise), as well
     * as the page load timestamp and creates initial states.
     */
    protected void initCookieStates(String plainCookieId, String etagCookieId) {
        CookieStates states = getCookieStates();
        CookieState plainState;
        ZombieCookie cookie = null;

        if (plainCookieId == null) {
            // may be overridden to UNSUPPORTED after javascript info received
            plainState = NEW;
        } else if ((cookie = lookupZombieCookie(plainCookieId)) != null) {
            if (cookie.hasParent()) {
                cookie = cookie.oldestRelative();
                plainState = REPLACED_BY_OLDER_PLAIN_COOKIE;
            } else {
                plainState = EXISTING;
            }
        } else {
            plainState = RESET;
        }

        if(cookie == null) {
            cookie = createZombieCookie();
        }

        setAndSaveZombieCookie(cookie);
        states.setPlainState(plainState);
        updateCookieStates(ETAG, etagCookieId);
    }

    protected void updateCookieStates(JsData jsData, boolean deviceCookieReceived) {
        CookieStates states = getCookieStates();

        if (!deviceCookieReceived) {
            // There has been a full round trip with the client, so we know for
            // sure if cookies are supported at this point.  We override
            // the previous value of NEW or RESTORED_FROM_*
            states.setPlainState(NOT_SUPPORTED);
        }

        if (jsData.getWebLocalStorageTest() != Boolean.TRUE) {
            states.setWebStorageState(NOT_SUPPORTED);
        }
    }

    protected void updateCookieStates(FlashData flashData) {
        CookieStates states = getCookieStates();

        if (flashData.getLsoStorageTest() != Boolean.TRUE) {
            states.setFlashState(NOT_SUPPORTED);
        }
    }

    protected void updateCookieStates(SilverlightData silverlightData) {
        CookieStates states = getCookieStates();

        if (silverlightData.getIsolatedStorageTest() != Boolean.TRUE) {
            states.setSilverlightState(NOT_SUPPORTED);
        }
    }

    protected void updateCookieStates(CookieType type, String cookieId) {
        CookieStates states = getCookieStates();
        ZombieCookie cookie;

        if (cookieId == null) {
            setHowCookieWasRestored(type);
        } else if (cookieId.equals(getZombieCookie().getId())) {
            states.set(type, EXISTING);
        } else if ((cookie = lookupZombieCookie(cookieId)) != null) {
            // valid cookie existed, but with a different value than the currently selected id
            setStatesAndBindCookies(type, cookie);
        } else {
            // Cookie existed, but with an invalid value
            states.set(type, RESET);
        }
    }

    private void setStatesAndBindCookies(CookieType candidateType, ZombieCookie candidateCookie) {
        CookieStates states = getCookieStates();
        ZombieCookie currentCookie = getZombieCookie();

        if (currentCookie.hasParent()) {
            //  The current selected zombie cookie, in theory, could have a
            //  new parent inserted from a seperate simultaneous page load
            //  in a different HTTP session.  We don't have a seperate state
            //  name for this, but in practice we never see it either.
            currentCookie = currentCookie.oldestRelative();
            log.error("found unexpected parent cookie={}", currentCookie.getId());
        }

        boolean usingCandidateParent = false;
        if (candidateCookie.hasParent()) {
            usingCandidateParent = true;
            candidateCookie = candidateCookie.oldestRelative();
        }

        if (candidateCookie.isOlder(currentCookie)) {
            states.set(candidateType, usingCandidateParent ? replacedFromOlderState(candidateType) : EXISTING);
            cascadeNewBestCookie(candidateType);
        } else {
            // Pick a replacementent state for the candidate cookie
            setHowCookieWasReplaced(candidateType);
        }

        // If the incomming candidate cookie had a parent equal to the current
        // cookie, we don't have to set a new value.
        if (!currentCookie.getId().equals(candidateCookie.getId())) {
            setAndSaveZombieCookie(currentCookie.linkTo(candidateCookie));
        }
    }

    private CookieState restoredFromState(CookieType restoringType) {

        CookieState state;
        switch (restoringType) {
            case SILVERLIGHT:
                state = RESTORED_FROM_SILVERLIGHT_COOKIE;
                break;
            case FLASH:
                state = RESTORED_FROM_FLASH_COOKIE;
                break;
            case WEB_STORAGE:
                state = RESTORED_FROM_WEB_STORAGE_COOKIE;
                break;
            case PLAIN:
                state = RESTORED_FROM_PLAIN_COOKIE;
                break;
            case ETAG:
                state = RESTORED_FROM_ETAG_COOKIE;
                break;
            default:
                throw new RuntimeException("Unreachable code");
        }
        return state;
    }

    private CookieState replacedFromOlderState(CookieType replacingType) {

        CookieState state;
        switch (replacingType) {
            case SILVERLIGHT:
                state = REPLACED_BY_OLDER_SILVERLIGHT_COOKIE;
                break;
            case FLASH:
                state = REPLACED_BY_OLDER_FLASH_COOKIE;
                break;
            case WEB_STORAGE:
                state = REPLACED_BY_OLDER_WEB_STORAGE_COOKIE;
                break;
            case PLAIN:
                state = REPLACED_BY_OLDER_PLAIN_COOKIE;
                break;
            case ETAG:
                state = REPLACED_BY_OLDER_ETAG_COOKIE;
                break;
            default:
                throw new RuntimeException("Unreachable code");
        }
        return state;
    }

    /*
     *  Call this on cookies that were not set**, or on cookies whose inception
     *  date is newer than the current best cookie.  If a cookie is being set
     *  to the value of another existing cookie, there may be more than one
     *  cookie type with the identical EXISTING best value.  We just pick the
     *  first match in the order that the CookeType enumeration is declared.
     *
     *  ** If the cookie was not previously set, there may not be another
     *     EXISTING cookie to override it's value.  In this case, we set the
     *     target's state to NEW.
     */
    private void setHowCookieWasRestoredOrReplaced(CookieType targetType, boolean isReplacement) {
        CookieStates states = getCookieStates();

        if (states.get(targetType) == NOT_SUPPORTED) {
            return;
        }

        CookieState targetState = NEW;

        for (CookieType otherType : CookieType.values()) {
            if (otherType == targetType) {
                continue;
            }

            //  If a cookie was replaced by it's own ancestor, we want to treat it the same as EXISTING.
            CookieState otherState = getCookieStates().get(otherType);
            CookieState replacedFromStateForOtherType = replacedFromOlderState(otherType);
            if (otherState == EXISTING || otherState == replacedFromStateForOtherType) {
                targetState = (isReplacement) ? replacedFromStateForOtherType : restoredFromState(otherType);
                break;
            }
        }

        states.set(targetType, targetState);
    }

    private void setHowCookieWasRestored(CookieType targetType) {
        setHowCookieWasRestoredOrReplaced(targetType, false);
    }

    private void setHowCookieWasReplaced(CookieType targetType) {
        setHowCookieWasRestoredOrReplaced(targetType, true);
    }

    private void cascadeNewBestCookie(CookieType newBestCookieType) {
        CookieStates states = getCookieStates();
        CookieState restoredFromState = restoredFromState(newBestCookieType);
        CookieState replacedFromState = replacedFromOlderState(newBestCookieType);

        // Terminology:
        //   restored:  means the cookie had no valid previous value
        //   replaced:  means the cookie had a valid previous value that was replaced
        //

        for (CookieType otherType : CookieType.values()) {

            CookieState otherState = states.get(otherType);

            if (otherType == newBestCookieType || otherState == null) {
                continue;
            }

            if (otherType == ETAG && otherState != RESET) {
                states.setEtagState(LINKED_BUT_NOT_SYNCED);
                continue;
            }

            switch (otherState) {
                case EXISTING:
                case REPLACED_BY_OLDER_SILVERLIGHT_COOKIE:
                case REPLACED_BY_OLDER_FLASH_COOKIE:
                case REPLACED_BY_OLDER_WEB_STORAGE_COOKIE:
                case REPLACED_BY_OLDER_PLAIN_COOKIE:
                case REPLACED_BY_OLDER_ETAG_COOKIE:
                    states.set(otherType, replacedFromState);
                    break;
                case NEW:
                case RESTORED_FROM_SILVERLIGHT_COOKIE:
                case RESTORED_FROM_FLASH_COOKIE:
                case RESTORED_FROM_WEB_STORAGE_COOKIE:
                case RESTORED_FROM_PLAIN_COOKIE:
                case RESTORED_FROM_ETAG_COOKIE:
                    states.set(otherType, restoredFromState);
                    break;
            }

        }
    }
}
