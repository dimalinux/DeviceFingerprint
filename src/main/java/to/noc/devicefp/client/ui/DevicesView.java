/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.DeviceCs;
import to.noc.devicefp.client.entity.UserAgentDataCs;
import static to.noc.devicefp.shared.ValDisplayUtil.nullToEmpty;

public class DevicesView extends Composite implements IsWidget {

    interface Binder extends UiBinder<HTMLPanel, DevicesView> {}
    private static final Binder BINDER = GWT.create(Binder.class);

    public interface FilterSubmit {
        void applyFilter(String filterText);
    }
    FilterSubmit filterSubmitter;

    interface Style extends CssResource {
        String hideSearch();
    }

    @UiField Style style;

    @UiField(provided=true)
    SimplePager pager;

    @UiField
    CellTable<DeviceCs> table;

    @UiField
    SimplePanel detailsPanel;

    @UiField
    DivElement helpDivFrame;

    @UiField
    DivElement helpDiv;

    @UiField
    InputElement searchInput;

    @UiField Button applyFilterButton;

    @SuppressWarnings("LeakingThisInConstructor")
    public DevicesView() {
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.RIGHT, pagerResources, false, 10, true);
        initWidget(BINDER.createAndBindUi(this));
        pager.setDisplay(table);
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
        init();
    }

    @SuppressWarnings("deprecation")
    public final void init() {

        table.addColumn(
                new TextColumn<DeviceCs>() {
                    @Override
                    public String getValue(DeviceCs savedDevice) {
                        return JsDate.create(savedDevice.getServerStamp().getTime()).toLocaleString();
                    }
                },
                "Local Time"
                );

        table.addColumn(
                new TextColumn<DeviceCs>() {

                    @Override
                    public String getValue(DeviceCs savedDevice) {
                        String host = savedDevice.getRemoteHost();
                        if (host == null) {
                            host = savedDevice.getIpAddress();
                        }
                        return host;
                    }
                },
                "Public Hostname");

        table.addColumn(
                new TextColumn<DeviceCs>() {

                    @Override
                    public String getValue(DeviceCs savedDevice) {
                        String cookieId = savedDevice.getCookieStates().getZombieCookie().getId();
                        cookieId = cookieId.replaceFirst("-.*", "");
                        return nullToEmpty(cookieId);
                    }
                },
                "Cookie ID");

        table.addColumn(
                new TextColumn<DeviceCs>() {
                    @Override
                    public String getValue(DeviceCs savedDevice) {
                        UserAgentDataCs uaData = savedDevice.getUserAgentData();
                        return (uaData != null) ?
                                nullToEmpty(uaData.getUaName()) : "?";
                    }
                },
                "UA Browser"
                );

        table.addColumn(
                new TextColumn<DeviceCs>() {
                    @Override
                    public String getValue(DeviceCs savedDevice) {
                        UserAgentDataCs uaData = savedDevice.getUserAgentData();
                        return (uaData != null) ?
                                nullToEmpty(uaData.getOsName()) : "?";
                    }
                },
                "UA OS"
                );
    }

    public CellTable<DeviceCs> getTable() {
        return table;
    }

    public void setDetailsWidget(Widget detailsWidget) {
        detailsPanel.setWidget(detailsWidget);
    }

    public void setDetailsVisible(boolean isVisible) {
        detailsPanel.setVisible(isVisible);
    }

    public void showHelp(String message) {
        helpDiv.setInnerText(message);
        UIObject.setVisible(helpDivFrame, true);
    }

    public void hideHelp() {
        UIObject.setVisible(helpDivFrame, false);
    }

    public void enableSearchBox(String initialFilter, FilterSubmit filterSubmitter) {
        this.filterSubmitter = filterSubmitter;
        if (initialFilter != null) {
            searchInput.setValue(initialFilter);
        }
        hideSearchElements(false);
    }

    @UiHandler("applyFilterButton")
    void onOpenAllPanelsButtonClicked(ClickEvent event) {
        hideSearchElements(true);
        if (filterSubmitter != null) {
            filterSubmitter.applyFilter(searchInput.getValue());
        }
    }

    void hideSearchElements(boolean hide) {
        if (hide) {
            searchInput.addClassName(style.hideSearch());
            applyFilterButton.addStyleName(style.hideSearch());
        } else {
            searchInput.removeClassName(style.hideSearch());
            applyFilterButton.removeStyleName(style.hideSearch());
        }
    }

}
