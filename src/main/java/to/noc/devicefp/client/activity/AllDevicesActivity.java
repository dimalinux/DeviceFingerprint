/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.activity;

import com.google.web.bindery.requestfactory.shared.Request;
import java.util.List;
import to.noc.devicefp.client.entity.DeviceCs;
import to.noc.devicefp.client.entity.DeviceProxy;
import to.noc.devicefp.client.ui.DevicesView.FilterSubmit;
import to.noc.devicefp.shared.AppRequestFactory;


public class AllDevicesActivity extends AbstractDevicesActivity {

    private final AppRequestFactory requests;
    private String filter = "hasJs";

    private FilterSubmit filterSubmit = new FilterSubmit() {
        @Override
        public void applyFilter(String newFilter) {
            AllDevicesActivity.this.filter = newFilter;
            AllDevicesActivity.this.reloadGrid();
        }
    };

    public AllDevicesActivity(final AppRequestFactory requests) {
        super(false);
        this.requests = requests;
        devicesView.enableSearchBox(filter, filterSubmit);
    }

    @Override
    protected void showDetails(DeviceCs device) {
        super.showDetails(device);
        devicesView.enableSearchBox(filter, filterSubmit);
    }

    @Override
    protected Request<List<DeviceProxy>> findDevices(int firstResult, int maxResults) {
        return requests.deviceRequest().findAdminView(filter, firstResult, maxResults);
    }

    @Override
    protected Request<Long> countDevices() {
        return requests.deviceRequest().countAdminView(filter);
    }

    @Override
    protected String getHelpText() {
        return "All Imprints";
    }

}
