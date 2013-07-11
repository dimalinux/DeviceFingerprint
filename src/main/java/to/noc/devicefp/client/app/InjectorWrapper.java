/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.app;

import com.google.gwt.core.client.GWT;

public class InjectorWrapper {

    public AppInjector getInjector() {
        return GWT.create(AppInjector.class);
    }
}
