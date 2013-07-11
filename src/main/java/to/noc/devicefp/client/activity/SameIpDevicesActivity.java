/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.activity;

import com.google.web.bindery.requestfactory.shared.Request;
import java.util.List;
import to.noc.devicefp.client.entity.DeviceProxy;
import to.noc.devicefp.client.jsni.CurrentDevice;
import to.noc.devicefp.shared.AppRequestFactory;


public class SameIpDevicesActivity extends AbstractDevicesActivity {

    private final AppRequestFactory requests;
    String helpText = "Imprint history of current IP address: " +
            CurrentDevice.instance().getIpAddress();

    public SameIpDevicesActivity(final AppRequestFactory requests) {
        super(false);
        this.requests = requests;
    }

    @Override
    protected Request<List<DeviceProxy>> findDevices(int firstResult, int maxResults) {
        return requests.deviceRequest().findWithSameIp(firstResult, maxResults);
    }

    @Override
    protected Request<Long> countDevices() {
        return requests.deviceRequest().countWithSameIp();
    }

    @Override
    protected String getHelpText() {
        return helpText;
    }

}
