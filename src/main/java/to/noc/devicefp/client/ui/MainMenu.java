/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import to.noc.devicefp.client.jsni.CurrentUser;
import to.noc.devicefp.client.place.*;

public class MainMenu implements PlaceChangeEvent.Handler {

    private static final ArrayList<MenuPlace> menuPlaces = new ArrayList<MenuPlace>();
    static {
        menuPlaces.add(new CurrentDevicePlace());
        menuPlaces.add(new LinkedDevicesPlace());
        menuPlaces.add(new SameIpDevicesPlace());
        if (CurrentUser.instance().isAdmin()) {
            menuPlaces.add(new AllDevicesPlace());
        }
        menuPlaces.add(new HelpPlace());
        menuPlaces.add(new FeedbackPlace());
    }

    public MainMenu() {
    }

    public static MenuPlace getDefaultPlace() {
        return menuPlaces.get(0);
    }

@Override
    public void onPlaceChange(PlaceChangeEvent event) {
        Place newPlace = event.getNewPlace();
        if (newPlace instanceof MenuPlace) {
            for (MenuPlace place : menuPlaces) {
                Element menuElement = RootPanel.get(place.getMenuId()).getElement();
                if (place.equals(newPlace)) {
                    menuElement.addClassName("currentMenuItem");
                } else {
                    menuElement.removeClassName("currentMenuItem");
                }
            }
        }
    }
}
