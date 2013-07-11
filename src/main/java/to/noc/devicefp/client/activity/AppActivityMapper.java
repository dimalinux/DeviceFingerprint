/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import java.util.logging.Logger;
import to.noc.devicefp.client.place.*;
import to.noc.devicefp.shared.AppRequestFactory;

public final class AppActivityMapper implements ActivityMapper {

    private static final Logger log = Logger.getLogger(AppActivityMapper.class.getName());

    private AppRequestFactory requests;

    private CurrentDeviceActivity currentDeviceActivity;
    private LinkedDevicesActivity linkedDevicesActivity;
    private SameIpDevicesActivity sameIpDevicesActivity;
    private AllDevicesActivity allDevicesActivity;
    private FeedbackActivity feedbackActivity;
    private HelpActivity helpActivity;

    @Inject
    public AppActivityMapper(AppRequestFactory requests) {
        //log.fine("AppMasterActivities created");
        this.requests = requests;

        //  We want to load the current device even if the user never visits the
        //  current device tab.  All other activities are lazy initialized.
        this.currentDeviceActivity = new CurrentDeviceActivity(requests);
    }

    private CurrentDeviceActivity getCurrentDeviceActivity() {
        return currentDeviceActivity;
    }

    private LinkedDevicesActivity getLinkedDevicesActivity() {
        if (linkedDevicesActivity == null) {
            linkedDevicesActivity = new LinkedDevicesActivity(requests);
        }
        return linkedDevicesActivity;
    }

    private SameIpDevicesActivity getSameIpDevicesActivity() {
        if (sameIpDevicesActivity == null) {
            sameIpDevicesActivity = new SameIpDevicesActivity(requests);
        }
        return sameIpDevicesActivity;
    }

    private AllDevicesActivity getAllDevicesActivity() {
        if (allDevicesActivity == null) {
            allDevicesActivity = new AllDevicesActivity(requests);
        }
        return allDevicesActivity;
    }

    private FeedbackActivity getFeedbackActivity() {
        if (feedbackActivity == null) {
            feedbackActivity = new FeedbackActivity(requests);
        }
        return feedbackActivity;
    }

    private HelpActivity getHelpActivity() {
        if (helpActivity == null) {
            helpActivity = new HelpActivity();
        }
        return helpActivity;
    }

    @Override
    public Activity getActivity(Place place) {

        //log.info("AppMasterActivities: getActivity(" + place + ")");

        if (place instanceof CurrentDevicePlace) {
            return getCurrentDeviceActivity();
        } else if (place instanceof LinkedDevicesPlace) {
            return getLinkedDevicesActivity();
        } else if (place instanceof SameIpDevicesPlace) {
            return getSameIpDevicesActivity();
        } else if (place instanceof AllDevicesPlace) {
            return getAllDevicesActivity();
        } else if (place instanceof FeedbackPlace) {
            return getFeedbackActivity();
        } else if (place instanceof HelpPlace) {
            return getHelpActivity();
        }

        log.warning("AppMasterActivities: no matching place found");
        return null;
    }
}
