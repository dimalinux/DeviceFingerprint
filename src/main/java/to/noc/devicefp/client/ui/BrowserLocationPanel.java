/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.BrowserLocationCs;
import to.noc.devicefp.client.entity.JsDataCs;
import to.noc.devicefp.client.entity.ReverseGeocodeCs;
import static com.google.gwt.geolocation.client.PositionError.*;
import static to.noc.devicefp.client.ui.TimeFormatSelector.TimeFormat.DEVICE_TZ;
import static to.noc.devicefp.shared.ValDisplayUtil.accuracyValue;
import static to.noc.devicefp.shared.ValDisplayUtil.coordinateValue;


public class BrowserLocationPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, BrowserLocationPanel> {}

    @UiField TableElement browserLocationTable;
    @UiField Label latLongLabel;
    @UiField Label accuracyRadiusLabel;
    @UiField LocationMap locationMap;
    @UiField(provided=true) TimeWithSelectableFormat timeStamp = new TimeWithSelectableFormat(DEVICE_TZ);

    @UiField Element reverseGeocodeHeaderRow;

    @UiField Element reverseGeocodeStatusRow;
    @UiField Label reverseGeocodeStatusLabel;

    @UiField Element reverseGeocodeLatLongRow;
    @UiField Label reverseGeocodeLatLongLabel;

    @UiField Element addressRow;
    @UiField Label addressLabel;

    @UiField Element addressTypeRow;
    @UiField Label addressTypeLabel;

    @UiField Element streetNumberRow;
    @UiField Label streetNumberLabel;

    @UiField Element routeRow;
    @UiField Label routeLabel;

    @UiField Element neighborhoodRow;
    @UiField Label neighborhoodLabel;

    @UiField Element localityRow;
    @UiField Label localityLabel;

    @UiField Element adminAreaLevel1Row;
    @UiField Label adminAreaLevel1Label;

    @UiField Element adminAreaLevel2Row;
    @UiField Label adminAreaLevel2Label;

    @UiField Element countryRow;
    @UiField Label countryLabel;

    @UiField Element postalCodeRow;
    @UiField Label postalCodeLabel;

    @UiField TableElement positionErrorTable;

    @UiField Element errorCodeRow;
    @UiField Label errorCodeLabel;

    @UiField Element errorMessageRow;
    @UiField Label errorMessageLabel;

    @UiField Element noResponseReceived;
    @UiField Element noResponseReceivedCurrentDeviceMessage;
    @UiField Element noResponseReceivedSavedDeviceMessage;

    @UiField Anchor fullDecodeAnchor;

    private static NumberFormat positionFmt = NumberFormat.getFormat("#.#######");

    private JsDataCs jsData;
    private BrowserLocationCs location;
    private ReverseGeocodeCs reverseGeocode;


    @SuppressWarnings("LeakingThisInConstructor")
    public BrowserLocationPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private void setFullDecodeAnchor(Double latitude, Double longitude) {
        boolean visible = (latitude != null && longitude != null);
        if (visible) {
            String url = "geodecode#" +
                    positionFmt.format(latitude) + "," + positionFmt.format(longitude);
            fullDecodeAnchor.setHref(url);
        }
        fullDecodeAnchor.setVisible(visible);
    }


    private static void setOptionalCoordinate(Element row, Label valueLabel, Double xPos, Double yPos) {
        boolean visible = false;
        if (xPos != null || yPos != null) {
            visible = true;
            valueLabel.setText(
                    coordinateValue(
                        positionFmt.format(xPos),
                        positionFmt.format(yPos),
                        true));
        }
        UIObject.setVisible(row, visible);
    }

    private static void setOptionalValue(Element row, Label valueLabel, Object value) {
        boolean visible = false;
        if (value != null) {
            visible = true;
            valueLabel.setText(value.toString());
        }
        UIObject.setVisible(row, visible);
    }

    private static String geolocationErrorCodeValue(Integer errorCode) {
        String codeStr = null;

        if (errorCode != null) {
            codeStr = errorCode.toString();

            switch (errorCode) {
                case UNKNOWN_ERROR:
                    codeStr += " (Unknown Error)";
                    break;
                case PERMISSION_DENIED:
                    codeStr += " (Permission Denied)";
                    break;
                case POSITION_UNAVAILABLE:
                    codeStr += " (Position Unavailable)";
                    break;
                case TIMEOUT:
                    codeStr += " (Timeout)";
                    break;
            }
        }

        return codeStr;
    }

    public void setValue(
            JsDataCs jsData,
            BrowserLocationCs location,
            ReverseGeocodeCs reverseGeocode,
            boolean isCurrentDevice) {
        // equality check safe. fields in instances of these classes do not change
        // after first init
        if (this.jsData == jsData
                && this.location == location
                && this.reverseGeocode == reverseGeocode) {
            return;
        }
        this.jsData = jsData;
        this.location = location;
        this.reverseGeocode = reverseGeocode;

        boolean browserLocationVisible = false;
        boolean positionErrorVisible = false;
        boolean noDataMessageVisible = location == null;

        if (location != null) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            if (latitude != null && longitude != null) {
                browserLocationVisible = true;
                latLongLabel.setText(coordinateValue(
                            positionFmt.format(latitude),
                            positionFmt.format(longitude),
                            true
                        ));
                accuracyRadiusLabel.setText(accuracyValue(location.getAccuracyRadius()));
                locationMap.setLocation(location, reverseGeocode);
                timeStamp.setValue(jsData, location.getStamp());
                setReverseGeolocationValues(location, reverseGeocode);
            } else if (location.getErrorCode() != null) {
                positionErrorVisible = true;
                setOptionalValue(errorCodeRow, errorCodeLabel, geolocationErrorCodeValue(location.getErrorCode()));
                setOptionalValue(errorMessageRow, errorMessageLabel, location.getErrorMessage());
            }
        }

        UIObject.setVisible(browserLocationTable, browserLocationVisible);
        UIObject.setVisible(positionErrorTable, positionErrorVisible);
        UIObject.setVisible(noResponseReceived, noDataMessageVisible);
        if (noDataMessageVisible) {
            UIObject.setVisible(noResponseReceivedCurrentDeviceMessage, isCurrentDevice);
            UIObject.setVisible(noResponseReceivedSavedDeviceMessage, !isCurrentDevice);
        }
    }

    private void setReverseGeolocationValues(BrowserLocationCs location, ReverseGeocodeCs reverseGeocode) {

        String status = null;
        Double latitude = null;
        Double longitude = null;
        String address = null;
        String addressType = null;
        String streetNumber = null;
        String route = null;
        String neighborhood = null;
        String locality = null;
        String adminAreaLevel1 = null;
        String adminAreaLevel2 = null;
        String country = null;
        String postalCode = null;
        boolean showHeaderRow = false;

        if (reverseGeocode != null) {
            showHeaderRow = true;

            status = reverseGeocode.getStatus();
            if ("OK".equals(status)) {
                status = null;  // only display status value for errors
            } else if (status != null) {
                status = status.replace("_", " ");
            }

            latitude = reverseGeocode.getLatitude();
            longitude = reverseGeocode.getLongitude();
            address = reverseGeocode.getAddress();
            addressType = reverseGeocode.getAddressType();
            streetNumber = reverseGeocode.getStreetNumber();
            route = reverseGeocode.getRoute();
            neighborhood = reverseGeocode.getNeighborhood();
            locality = reverseGeocode.getLocality();
            adminAreaLevel1 = reverseGeocode.getAdministrativeAreaLevel1();
            adminAreaLevel2 = reverseGeocode.getAdministrativeAreaLevel2();
            country = reverseGeocode.getCountry();
            postalCode = reverseGeocode.getPostalCode();
        }

        UIObject.setVisible(reverseGeocodeHeaderRow, showHeaderRow);
        setOptionalValue(reverseGeocodeStatusRow, reverseGeocodeStatusLabel, status);
        setOptionalCoordinate(reverseGeocodeLatLongRow, reverseGeocodeLatLongLabel, latitude, longitude);
        setFullDecodeAnchor(location.getLatitude(), location.getLongitude());
        setOptionalValue(addressRow, addressLabel, address);
        setOptionalValue(addressTypeRow, addressTypeLabel, addressType);
        setOptionalValue(streetNumberRow, streetNumberLabel, streetNumber);
        setOptionalValue(routeRow, routeLabel, route);
        setOptionalValue(neighborhoodRow, neighborhoodLabel, neighborhood);
        setOptionalValue(localityRow, localityLabel, locality);
        setOptionalValue(adminAreaLevel1Row, adminAreaLevel1Label, adminAreaLevel1);
        setOptionalValue(adminAreaLevel2Row, adminAreaLevel2Label, adminAreaLevel2);
        setOptionalValue(countryRow, countryLabel, country);
        setOptionalValue(postalCodeRow, postalCodeLabel, postalCode);
    }

}
