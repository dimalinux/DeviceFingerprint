/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.TcpSynDataCs;

public class TcpSynPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, TcpSynPanel> {}

    @UiField Label receivedTimeToLiveLabel;
    @UiField Label ipTypeOfServiceLabel;
    @UiField Label ipTotalLengthLabel;
    @UiField Label ipFlagsLabel;
    @UiField Label tcpFlagsLabel;
    @UiField Label tcpWindowSizeLabel;
    @UiField SimplePanel tcpOptionsPanel;

    private TcpSynDataCs tcpSynData;


    @SuppressWarnings("LeakingThisInConstructor")
    public TcpSynPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void setValue(TcpSynDataCs tcpSynData) {

        if (this.tcpSynData == tcpSynData) {
            return;
        }
        this.tcpSynData = tcpSynData;

        receivedTimeToLiveLabel.setText(Short.toString(tcpSynData.getTtl()));
        ipTypeOfServiceLabel.setText("0x" + Integer.toHexString(tcpSynData.getTos()));
        ipTotalLengthLabel.setText(Integer.toString(tcpSynData.getLength()));

        String ipFlags = tcpSynData.getIpFlags();
        ipFlags = (ipFlags != null) ? ipFlags.replace("DF", "Don't Fragment") : "";
        ipFlagsLabel.setText(ipFlags);

        String tcpFlags = tcpSynData.getTcpFlags();
        // We occasionally see SEW (SYN, ECN, CWR) from bots. Until I see something
        // other than "S" from a real browser, we'll just leave the Tcpdump
        // abbreviations for unusual flag combinations.
        tcpFlags = (tcpFlags != null) ? tcpFlags.replaceFirst("\\bS\\b", "SYN") : "";
        tcpFlagsLabel.setText(tcpFlags);

        tcpWindowSizeLabel.setText(Integer.toString(tcpSynData.getWindowSize()));

        String tcpOptions = tcpSynData.getTcpOptions();
        if (tcpOptions != null) {
            String[] tcpOptsArray = tcpOptions.split(",");

            ListWidget list = new ListWidget(true);
            for (String option : tcpOptsArray) {
                if (option.equals("sackOK")) {
                    option = "Selective ACK Permitted";
                }
                if (option.startsWith("mss")) {
                    option = "Max Segment Size:" + option.substring(3);
                } else if (option.equals("nop")) {
                    option = "No Operation";
                } else if (option.startsWith("wscale")) {
                    option = "Window Scale:" + option.substring(6);
                } else if (option.startsWith("TS val ")) {
                    option = "Timestamp Value:" + option.substring(6).replace(" ecr 0", "");
                } else if (option.startsWith("eol")) {
                    option = "End of Option List";
                }
                list.add(new ListItemWidget(option));

            }
            tcpOptionsPanel.setWidget(list);
        } else {
            tcpOptionsPanel.setWidget(new Label("[empty]"));
        }
    }

}
