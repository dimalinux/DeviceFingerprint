/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.activity;

import com.google.web.bindery.requestfactory.shared.Request;
import java.util.List;
import to.noc.devicefp.client.entity.DeviceProxy;
import to.noc.devicefp.client.ui.DeviceDetailsView.DeleteHandler;
import to.noc.devicefp.shared.AppRequestFactory;


public class LinkedDevicesActivity extends AbstractDevicesActivity implements DeleteHandler {

    private final AppRequestFactory requests;

    public LinkedDevicesActivity(final AppRequestFactory requests) {
        super(true);
        this.requests = requests;
    }

    @Override
    protected Request<List<DeviceProxy>> findDevices(int firstResult, int maxResults) {
        return requests.deviceRequest().findSaved(firstResult, maxResults);
    }

    @Override
    protected Request<Long> countDevices() {
        return requests.deviceRequest().countSaved();
    }

    @Override
    protected String getHelpText() {
        return "Imprints linked to the current zombie cookie. " +
               "Log in for imprints linked to your user across hosts.";
    }
}
