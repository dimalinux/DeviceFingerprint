/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.JsDataCs;
import to.noc.devicefp.client.entity.MaxMindLocationCs;
import static to.noc.devicefp.client.ui.TimeFormatSelector.TimeFormat.DEVICE_TZ;
import static to.noc.devicefp.shared.ValDisplayUtil.*;

public class MaxMindPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, MaxMindPanel> {}

    @UiField Label latLongLabel;
    @UiField Label accuracyRadiusLabel;

    @UiField LocationMap locationMap;
    @UiField(provided=true) TimeWithSelectableFormat lookupTime = new TimeWithSelectableFormat(DEVICE_TZ);

    @UiField TableRowElement continentRow;
    @UiField Label continentLabel;

    @UiField TableRowElement countryRow;
    @UiField Label countryLabel;

    @UiField TableRowElement regionRow;
    @UiField Label regionLabel;

    @UiField TableRowElement cityRow;
    @UiField Label cityLabel;

    @UiField TableRowElement areaCodeRow;
    @UiField Label areaCodeLabel;

    @UiField TableRowElement metroCodeRow;
    @UiField Label metroCodeLabel;

    @UiField TableRowElement postalCodeRow;
    @UiField Label postalCodeLabel;

    @UiField TableRowElement timeZoneRow;
    @UiField Label timeZoneLabel;

    @UiField TableRowElement ispRow;
    @UiField Label ispLabel;

    @UiField TableRowElement orgRow;
    @UiField Label orgLabel;

    @UiField TableRowElement asNumRow;
    @UiField Label asNumLabel;

    @UiField TableRowElement domainRow;
    @UiField Label domainLabel;

    @UiField TableRowElement netSpeedRow;
    @UiField Label netSpeedLabel;

    @UiField TableRowElement userTypeRow;
    @UiField Label userTypeLabel;

    @UiField TableRowElement errorRow;
    @UiField Label errorLabel;

    private JsDataCs jsData;
    private MaxMindLocationCs location;

    @SuppressWarnings("LeakingThisInConstructor")
    public MaxMindPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private String continentName(String continentCode) {
        String name = null;

        if (continentCode != null) {
            if ("NA".equals(continentCode)) {
                name = "North America";
            } else if ("EU".equals(continentCode)) {
                name = "Europe";
            } else if ("AS".equals(continentCode)) {
                name = "Asia";
            } else if ("SA".equals(continentCode)) {
                name = "South America";
            } else if ("OC".equals(continentCode)) {
                name = "Oceania";
            } else if ("AF".equals(continentCode)) {
                name = "Africa";
            } else if ("AN".equals(continentCode)) {
                name = "Antarctica";
            } else {
                name = continentCode;
            }
        }
        return name;
    }

    private static void setLabel(TableRowElement row, Label valueLabel, Object value) {
        boolean visible = false;
        if (value != null) {
            visible = true;
            valueLabel.setText(nullToEmpty(value.toString()));
        }
        UIObject.setVisible(row, visible);
    }

    private static void setLabelWithConf(TableRowElement row, Label valueLabel, String value, String conf) {
        boolean visible = false;
        if (value != null) {
            visible = true;
            valueLabel.setText(valueWithConfidence(value, conf));
        }
        UIObject.setVisible(row, visible);
    }


    public void setValue(JsDataCs jsData, MaxMindLocationCs location) {
        // equality check safe. Fields in instances of jsData and location do not change
        if (this.jsData == jsData && this.location == location) {
            return;
        }
        this.jsData = jsData;
        this.location = location;

        latLongLabel.setText(
                coordinateValue(
                    location.getLatitude(), location.getLongitude(), true)
                );
        accuracyRadiusLabel.setText(accuracyValue(location.getAccuracyRadius()));

        locationMap.setLocation(location);
        lookupTime.setValue(jsData, location.getStamp());

        setLabel(continentRow, continentLabel, continentName(location.getContinent()));
        setLabelWithConf(countryRow, countryLabel,
                location.getCountryName(), location.getCountryConf());
        setLabelWithConf(regionRow, regionLabel,
                location.getRegionName(), location.getRegionConf());
        setLabelWithConf(cityRow, cityLabel, location.getCity(), location.getCityConf());
        setLabel(areaCodeRow, areaCodeLabel, location.getAreaCode());
        setLabel(metroCodeRow, metroCodeLabel, location.getMetroCode());
        setLabelWithConf(postalCodeRow, postalCodeLabel,
                location.getPostalCode(), location.getPostalConf());
        setLabel(timeZoneRow, timeZoneLabel, location.getTimeZone());
        setLabel(ispRow, ispLabel, location.getIsp());
        setLabel(orgRow, orgLabel, location.getOrg());
        setLabel(asNumRow, asNumLabel, location.getAsnum());
        setLabel(domainRow, domainLabel, location.getDomain());
        setLabel(netSpeedRow, netSpeedLabel, location.getNetSpeed());
        setLabel(userTypeRow, userTypeLabel, location.getUserType());
        setLabel(errorRow, errorLabel, location.getError());
    }

}
