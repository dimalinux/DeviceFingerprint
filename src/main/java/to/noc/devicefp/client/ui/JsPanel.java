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
import to.noc.devicefp.shared.ValDisplayUtil.BoolType;
import static to.noc.devicefp.shared.ValDisplayUtil.BoolType.*;
import static to.noc.devicefp.shared.ValDisplayUtil.boolToString;

public class JsPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<HTMLPanel, JsPanel> {}

    @UiField TableRowElement userAgentRow;
    @UiField Label userAgentLabel;

    @UiField TableRowElement appNameRow;
    @UiField Label appNameLabel;

    @UiField TableRowElement appCodeNameRow;
    @UiField Label appCodeNameLabel;

    @UiField TableRowElement appVersionRow;
    @UiField Label appVersionLabel;

    @UiField TableRowElement appMinorVersionRow;
    @UiField Label appMinorVersionLabel;

    @UiField TableRowElement buildIdRow;
    @UiField Label buildIdLabel;

    @UiField TableRowElement platformRow;
    @UiField Label platformLabel;

    @UiField TableRowElement cpuClassRow;
    @UiField Label cpuClassLabel;

    @UiField TableRowElement osCpuRow;
    @UiField Label osCpuLabel;

    @UiField TableRowElement productRow;
    @UiField Label productLabel;

    @UiField TableRowElement productSubRow;
    @UiField Label productSubLabel;

    @UiField TableRowElement vendorRow;
    @UiField Label vendorLabel;

    @UiField TableRowElement vendorSubRow;
    @UiField Label vendorSubLabel;

    @UiField TableRowElement languageRow;
    @UiField Label languageLabel;

    @UiField TableRowElement userLanguageRow;
    @UiField Label userLanguageLabel;

    @UiField TableRowElement browserLanguageRow;
    @UiField Label browserLanguageLabel;

    @UiField TableRowElement systemLanguageRow;
    @UiField Label systemLanguageLabel;

    @UiField TableRowElement doNotTrackRow;
    @UiField Label doNotTrackLabel;

    @UiField TableRowElement msDoNotTrackRow;
    @UiField Label msDoNotTrackLabel;

    @UiField TableRowElement javaRow;
    @UiField Label javaLabel;

    @UiField TableRowElement geolocationRow;
    @UiField Label geolocationLabel;

    @UiField TableRowElement cookiesRow;
    @UiField Label cookiesLabel;

    @UiField TableRowElement cookieTestRow;
    @UiField Label cookieTestLabel;

    @UiField TableRowElement webStorageRow;
    @UiField Label webStorageLabel;

    @UiField TableRowElement webStorageTestRow;
    @UiField Label webStorageTestLabel;

                    
    @SuppressWarnings("LeakingThisInConstructor")
    public JsPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }       
    
    private static void setLabel(TableRowElement row, Label valueLabel, String value) {
        boolean visible = false;
        if (value != null) {
            visible = true;
            if (value.length() == 0) {
                value = "[field present but empty]";
            }
            valueLabel.setText(value.toString());
        }
        UIObject.setVisible(row, visible);
    }
        
    private static void setLabel(TableRowElement row, Label valueLabel, Boolean value, BoolType boolType) {
        boolean visible = false;
        if (value != null) {
            visible = true;            
            valueLabel.setText(boolToString(value, boolType));
        }
        UIObject.setVisible(row, visible);
    }
            
    public void setValue(JsDataCs jsData) {
        setLabel(userAgentRow, userAgentLabel, jsData.getUserAgent());
        setLabel(appNameRow, appNameLabel, jsData.getAppName());
        setLabel(appCodeNameRow, appCodeNameLabel, jsData.getAppCodeName());
        setLabel(appVersionRow, appVersionLabel, jsData.getAppVersion());
        setLabel(appMinorVersionRow, appMinorVersionLabel, jsData.getAppMinorVersion());
        setLabel(buildIdRow, buildIdLabel, jsData.getBuildID());
        setLabel(platformRow, platformLabel, jsData.getPlatform());
        setLabel(cpuClassRow, cpuClassLabel, jsData.getCpuClass());
        setLabel(osCpuRow, osCpuLabel, jsData.getOscpu());
        setLabel(productRow, productLabel, jsData.getProduct());
        setLabel(productSubRow, productSubLabel, jsData.getProductSub());
        setLabel(vendorRow, vendorLabel, jsData.getVendor());
        setLabel(vendorSubRow, vendorSubLabel, jsData.getVendorSub());
        setLabel(languageRow, languageLabel, jsData.getLanguage());
        setLabel(userLanguageRow, userLanguageLabel, jsData.getUserLanguage());
        setLabel(browserLanguageRow, browserLanguageLabel, jsData.getBrowserLanguage());
        setLabel(systemLanguageRow, systemLanguageLabel, jsData.getSystemLanguage());
        setLabel(doNotTrackRow, doNotTrackLabel, jsData.getDoNotTrack());
        setLabel(msDoNotTrackRow, msDoNotTrackLabel, jsData.getMsDoNotTrack(), EnabledDisabled);
        setLabel(javaRow, javaLabel, jsData.getJavaEnabled(), EnabledDisabled);
        setLabel(geolocationRow, geolocationLabel, jsData.getGeolocationCapable(), YesNo);
        setLabel(cookiesRow, cookiesLabel, jsData.getCookiesEnabled(), EnabledDisabled);
        setLabel(cookieTestRow, cookieTestLabel, jsData.getCookiesTest(), PassFail);
        setLabel(webStorageRow, webStorageLabel, jsData.getWebLocalStorageCapable(), YesNo);
        setLabel(webStorageTestRow, webStorageTestLabel, jsData.getWebLocalStorageTest(), PassFail);
    }

}
