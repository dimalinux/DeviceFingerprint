/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.DnsDataCs;
import to.noc.devicefp.client.entity.MaxMindLocationCs;
import static to.noc.devicefp.shared.ValDisplayUtil.nullToEmpty;
import static to.noc.devicefp.shared.ValDisplayUtil.valueWithConfidence;

public class DnsPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, DnsPanel> {}

    @UiField Label publicDnsIpLabel;
    @UiField Label publicDnsHostLabel;
    @UiField Label clientDidIPv6LookupLabel;

    @UiField TableRowElement countryRow;
    @UiField Label countryLabel;

    @UiField TableRowElement regionRow;
    @UiField Label regionLabel;

    @UiField TableRowElement cityRow;
    @UiField Label cityLabel;

    @UiField TableRowElement ispRow;
    @UiField Label ispLabel;

    @UiField TableRowElement orgRow;
    @UiField Label orgLabel;

    @UiField TableRowElement asNumRow;
    @UiField Label asNumLabel;

    @UiField TableRowElement domainRow;
    @UiField Label domainLabel;

    @UiField TableRowElement errorRow;
    @UiField Label errorLabel;

    private DnsDataCs dnsData;

    @SuppressWarnings("LeakingThisInConstructor")
    public DnsPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
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


    public void setValue(DnsDataCs dnsData) {
        // equality check safe.  fields in instances do not change after initilized
        if (this.dnsData == dnsData) {
            return;
        }
        this.dnsData = dnsData;

        String hostName = dnsData.getHostName();
        String dnsIp = dnsData.getSourceIp();

        if (dnsIp != null && (hostName == null || dnsIp.equals(hostName))) {
            hostName = "no reverse DNS entry";
        }

        publicDnsHostLabel.setText(hostName);
        publicDnsIpLabel.setText(dnsIp);
        clientDidIPv6LookupLabel.setText(Boolean.toString(dnsData.getIpv6RequestMade()));


        MaxMindLocationCs location = dnsData.getMaxMindLocation();
        if (location != null) {
            setLabelWithConf(countryRow, countryLabel, location.getCountryName(), location.getCountryConf());
            setLabelWithConf(regionRow, regionLabel, location.getRegionName(), location.getRegionConf());
            setLabelWithConf(cityRow, cityLabel, location.getCity(), location.getCityConf());
            setLabel(ispRow, ispLabel, location.getIsp());
            setLabel(orgRow, orgLabel, location.getOrg());
            setLabel(asNumRow, asNumLabel, location.getAsnum());
            setLabel(domainRow, domainLabel, location.getDomain());
            setLabel(errorRow, errorLabel, location.getError());
        } else {
            UIObject.setVisible(countryRow, false);
            UIObject.setVisible(regionRow, false);
            UIObject.setVisible(cityRow, false);
            UIObject.setVisible(ispRow, false);
            UIObject.setVisible(orgRow, false);
            UIObject.setVisible(asNumRow, false);
            UIObject.setVisible(domainRow, false);
            UIObject.setVisible(errorRow, false);
        }
    }

}
