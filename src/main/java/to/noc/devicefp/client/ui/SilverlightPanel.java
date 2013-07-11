/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.SilverlightDataCs;
import static to.noc.devicefp.shared.ValDisplayUtil.*;

public class SilverlightPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, SilverlightPanel> {}

    @UiField Label osVersionLabel;
    @UiField Label clrVersionLabel;
    @UiField Label processorCoresLabel;
    @UiField Label tickCountLabel;
    @UiField Label isolatedStorageEnabledLabel;
    @UiField Label isolatedStorageTestLabel;

    private SilverlightDataCs silverlightData;

    @SuppressWarnings("LeakingThisInConstructor")
    public SilverlightPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void setValue(SilverlightDataCs silverlightData) {
        // equality check safe. fields in silverlightData instances do not change
        if(this.silverlightData == silverlightData) {
            return;
        }
        this.silverlightData = silverlightData;

        osVersionLabel.setText(nullToEmpty(silverlightData.getOsVersion()));
        clrVersionLabel.setText(nullToEmpty(silverlightData.getClrVersion()));
        processorCoresLabel.setText(nullToEmpty(silverlightData.getProcessorCount()));
        tickCountLabel.setText(nullToEmpty(silverlightData.getSysUptimeMs()));

        Boolean storageEnabled = silverlightData.getIsolatedStorageEnabled();
        isolatedStorageEnabledLabel.setText(boolToEnabled(storageEnabled));

        String testResult = (storageEnabled == Boolean.TRUE)
                ? boolToPassFail(silverlightData.getIsolatedStorageTest())
                : "n/a";
        isolatedStorageTestLabel.setText(testResult);
    }

}
