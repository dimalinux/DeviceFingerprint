/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.place;

import com.google.gwt.place.shared.Place;

public abstract class MenuPlace extends Place {

    public abstract String getMenuId();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MenuPlace)) {
            return false;
        }

        MenuPlace menuPlace = (MenuPlace) o;

        return getMenuId().equals(menuPlace.getMenuId());

    }

    @Override
    public int hashCode() {
        return getMenuId().hashCode();
    }
}
