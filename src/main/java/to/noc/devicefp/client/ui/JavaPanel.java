/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.JavaDataCs;

public class JavaPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, JavaPanel> {}

    @UiField TableElement javaValuesTable;
    @UiField Label javaVendorLabel;
    @UiField Label vendorUrlLabel;
    @UiField Label javaVersionLabel;
    @UiField Label osArchLabel;
    @UiField Label osNameLabel;
    @UiField Label osVersionLabel;
    @UiField Label appletDidNotLoad;

    private JavaDataCs javaData;

    @SuppressWarnings("LeakingThisInConstructor")
    public JavaPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void setValue(JavaDataCs javaData, boolean isCurrentDevice) {

        if (javaData != null) {
            // equality check safe. instance fields do not change after initialized
            if (this.javaData == javaData) {
                return;
            }
            this.javaData = javaData;

            javaVendorLabel.setText(javaData.getJavaVendor());
            vendorUrlLabel.setText(javaData.getJavaVendorUrl());
            javaVersionLabel.setText(javaData.getJavaVersion());
            osArchLabel.setText(javaData.getOsArch());
            osNameLabel.setText(javaData.getOsName());
            osVersionLabel.setText(javaData.getOsVersion());

            UIObject.setVisible(javaValuesTable, true);
            appletDidNotLoad.setVisible(false);

        } else {
            String message = "Javascript reports that Java is enabled, but plugin ";
            message += (isCurrentDevice) ? "has not loaded." : "did not load.";
            appletDidNotLoad.setText(message);

            UIObject.setVisible(javaValuesTable, false);
            appletDidNotLoad.setVisible(true);
        }
    }

}
