/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import java.util.logging.Logger;
import to.noc.devicefp.client.jsni.CurrentUser;


public class AppPlaceHistoryMapper implements PlaceHistoryMapper {

    private static final Logger log = Logger.getLogger(AppPlaceHistoryMapper.class.getName());
    private static boolean isAdminUser = CurrentUser.instance().isAdmin();

    @Override
    public Place getPlace(String placeName) {

        if (placeName != null) {
            if ("!Current".equals(placeName)) {
                return new CurrentDevicePlace();
            } else if ("!Linked".equals(placeName)) {
                return new LinkedDevicesPlace();
            } else if ("!SameIp".equals(placeName)) {
                return new SameIpDevicesPlace();
            } else if (isAdminUser && "!All".equals(placeName)) {
                return new AllDevicesPlace();
            } else if ("!Feedback".equals(placeName)) {
                return new FeedbackPlace();
            } else if (placeName.matches("!?Help(:.*)?")) {
                return new HelpPlace();
            }

            log.severe("No place matched");
        } else {
            log.severe("token is null");
        }
        return null;
    }

    @Override
    public String getToken(Place place)
    {
        if (place == null) {
            log.severe("place is null");
            return null;
        }

        if (place instanceof CurrentDevicePlace) {
            return "!Current";
        } else if (place instanceof LinkedDevicesPlace) {
            return "!Linked";
        } else if (place instanceof SameIpDevicesPlace) {
            return "!SameIp";
        } else if (place instanceof AllDevicesPlace) {
            return "!All";
        } else if (place instanceof FeedbackPlace) {
            return "!Feedback";
        } else if (place instanceof HelpPlace) {
            return "!Help";
        }

        log.severe("No token matched");
        return null;
    }

}
