/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.*;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import to.noc.devicefp.client.entity.DeviceCs;
import to.noc.devicefp.client.entity.DeviceProxy;
import to.noc.devicefp.client.jsni.CurrentDevice;
import to.noc.devicefp.client.ui.DeviceDetailsView;
import to.noc.devicefp.client.ui.DeviceDetailsView.DeleteHandler;
import to.noc.devicefp.client.ui.DevicesView;


public abstract class AbstractDevicesActivity extends AbstractActivity implements DeleteHandler {

    private static final Logger log = Logger.getLogger(AbstractDevicesActivity.class.getName());

    private final SingleSelectionModel<DeviceCs> singleSelectModel;
    private final DevicesView devicesView = new DevicesView();
    private final DeviceDetailsView deviceDetailsView = DeviceDetailsView.instance();
    private final boolean enableDelete;

    protected abstract Request<Long> countDevices();
    protected abstract Request<List<DeviceProxy>> findDevices(int firstResult, int maxResults);
    protected abstract String getHelpText();

    //
    // Override this method if you need a delete handler
    //
    protected Request<Void> markDeleted(Long deviceId) {
        return null;
    }

    public AbstractDevicesActivity(boolean enableDelete) {
        this.enableDelete = enableDelete;
        final CellTable<DeviceCs> table = devicesView.getTable();

        table.addRangeChangeHandler(
                new RangeChangeEvent.Handler() {
                    @Override
                    public void onRangeChange(RangeChangeEvent event) {
                        updateGridRange();
                    }
                });

        // Inherit the view's key provider
        ProvidesKey<DeviceCs> keyProvider = table.getKeyProvider();
        singleSelectModel = new SingleSelectionModel<DeviceCs>(keyProvider);
        table.setSelectionModel(singleSelectModel);

        singleSelectModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                DeviceCs selectedDevice = singleSelectModel.getSelectedObject();
                if (selectedDevice != null) {
                    showDetails(selectedDevice);
                }
            }
        });
    }


    @Override
    public void start(AcceptsOneWidget display, EventBus eventBus) {
        display.setWidget(devicesView);

        deviceDetailsView.setDeleteHandler(enableDelete ? this : null);
        devicesView.setDetailsVisible(false);
        devicesView.showHelp(getHelpText());
        devicesView.setDetailsWidget(deviceDetailsView);

        countDevices().fire(new Receiver<Long>() {

            @Override
            public void onSuccess(Long count) {
                //log.info("countSaved=" + count);
                boolean isExact = true;
                devicesView.getTable().setRowCount(count.intValue(), isExact);
            }

            @Override
            public void onFailure(ServerFailure error) {
                String message = "countSaved onFailure: " + error.getMessage();
                log.warning(message);
            }

            @Override
            public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                for (ConstraintViolation<?> vio : violations) {
                    String message = "countSaved onConstraintViolation: " + vio.getMessage();
                    log.warning(message);
                }
            }
        });

        // We're doing two simultaneous server requests (one for the current page
        // and one for the current count
        updateGridRange();
    }


    private void showDetails(DeviceCs device) {
        if (device != null) {
            DeviceCs currentDevice = CurrentDevice.instance();
            if (currentDevice.getId().equals(device.getId())) {
                // The local version of the current device may be more
                // up-to-date, so use it.
                device = currentDevice;
            }
            deviceDetailsView.setDevice(device, false);
            devicesView.hideHelp();
            devicesView.setDetailsVisible(true);
        } else {
            devicesView.setDetailsVisible(false);
            devicesView.showHelp(getHelpText());
        }
    }


    private void updateGridRange() {

        final Range range = devicesView.getTable().getVisibleRange();

        //log.info("updateGridRange: range="+range.toString());

        int start = range.getStart();
        int len = range.getLength();

        findDevices(start, len).with(
                        "requestHeaders",
                        "userAgentData",
                        "browserLocation",
                        "reverseGeocode",
                        "maxMindLocation",
                        "jsData",
                        "displayData",
                        "silverlightData",
                        "flashData",
                        "javaData",
                        "plugins",
                        "dnsData",
                        "dnsData.maxMindLocation",
                        "tcpSynData",
                        "cookieStates",
                        "cookieStates.zombieCookie").fire(
                new Receiver<List<DeviceProxy>>() {

                    @Override
                    public void onSuccess(List<DeviceProxy> values) {
                        devicesView.getTable().setRowData(range.getStart(), values);

                        /* Grid appears to be smart enough to keep the previously
                         * selected value if it's still in the new grid.
                         */
                        showDetails(singleSelectModel.getSelectedObject());
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        String message = "findDevices onFailure: " + error.getMessage();
                        log.warning(message);
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            String message = "findDevices onConstraintViolation: " + vio.getMessage();
                            log.warning(message);
                        }
                    }
                }
        );
    }


    @Override
    public void deleteButtonClicked() {

        DeviceProxy device = (DeviceProxy)singleSelectModel.getSelectedObject();
        singleSelectModel.setSelected(device, false);
        showDetails(null);

        Request<Void> deleteRequest = markDeleted(device.getId());
        if (deleteRequest == null) {
            log.severe("Missing markDeleted override");
            return;
        }

        deleteRequest.fire(new Receiver<Void>() {

            @Override
            public void onSuccess(Void ignore) {
                if (devicesView != null) {
                    CellTable<DeviceCs> table = devicesView.getTable();
                    table.setRowCount(table.getRowCount()-1);
                    updateGridRange();
                }
            }

            @Override
            public void onFailure(ServerFailure error) {
                log.warning("markDeleted Failure: " + error.getMessage());
            }
        });
    }

}
