/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.FlashDataCs;
import static to.noc.devicefp.shared.ValDisplayUtil.*;


public class FlashPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, FlashPanel> {}

    @UiField Label versionLabel;
    @UiField Label manufacturerLabel;
    @UiField Label languageLabel;
    @UiField Label osLabel;
    @UiField Label cpuArchLabel;
    @UiField Label supports32bitLabel;
    @UiField Label supports64bitLabel;
    @UiField Label screenColorLabel;
    @UiField Label touchscreenTypeLabel;
    @UiField Label screenResolutionLabel;
    @UiField Label lsoStorageTestLabel;

    @UiField TableRowElement pixelAspectRatioRow;
    @UiField Label pixelAspectRatioLabel;

    @UiField TableRowElement screenDpiRow;
    @UiField Label screenDpiLabel;

    @UiField TableRowElement avHardwareDisabledRow;
    @UiField Label avHardwareDisabledLabel;

    @UiField TableRowElement fileReadDisabledRow;
    @UiField Label fileReadDisabledLabel;

    @UiField TableRowElement hasPrintingRow;
    @UiField Label hasPrintingLabel;

    @UiField TableRowElement hasAccessibilityRow;
    @UiField Label hasAccessibilityLabel;

    @UiField TableRowElement hasAudioRow;
    @UiField Label hasAudioLabel;

    @UiField TableRowElement hasMp3Row;
    @UiField Label hasMp3Label;

    @UiField TableRowElement hasEmbeddedVideoRow;
    @UiField Label hasEmbeddedVideoLabel;

    @UiField TableRowElement hasScreenBroadcastRow;
    @UiField Label hasScreenBroadcastLabel;

    @UiField TableRowElement hasScreenPlaybackRow;
    @UiField Label hasScreenPlaybackLabel;

    @UiField TableRowElement hasStreamingAudioRow;
    @UiField Label hasStreamingAudioLabel;

    @UiField TableRowElement hasStreamingVideoRow;
    @UiField Label hasStreamingVideoLabel;

    @UiField TableRowElement hasAudioEncoderRow;
    @UiField Label hasAudioEncoderLabel;

    @UiField TableRowElement hasVideoEncoderRow;
    @UiField Label hasVideoEncoderLabel;

    @UiField TableRowElement hasInputMethodEditorRow;
    @UiField Label hasInputMethodEditorLabel;

    @UiField TableRowElement hasMaxLevelIDCRow;
    @UiField Label hasMaxLevelIDCLabel;

    @UiField TableRowElement playerTypeRow;
    @UiField Label playerTypeLabel;

    @UiField TableRowElement isDebuggerRow;
    @UiField Label isDebuggerLabel;

    @UiField TableRowElement hasTlsRow;
    @UiField Label hasTlsLabel;

    private FlashDataCs flashData;

    // We can't set this array until the UiField's are bound in the constructor
    private TableRowElement[] optionalRows;
    private void configOptionalRows() {
        optionalRows = new TableRowElement[]{
            pixelAspectRatioRow,
            screenDpiRow,
            avHardwareDisabledRow,
            fileReadDisabledRow,
            hasPrintingRow,
            hasAccessibilityRow,
            hasAudioRow,
            hasMp3Row,
            hasEmbeddedVideoRow,
            hasScreenBroadcastRow,
            hasScreenPlaybackRow,
            hasStreamingAudioRow,
            hasStreamingVideoRow,
            hasAudioEncoderRow,
            hasVideoEncoderRow,
            hasInputMethodEditorRow,
            hasMaxLevelIDCRow,
            playerTypeRow,
            isDebuggerRow,
            hasTlsRow
        };
    }

    private boolean verboseInfo = false;
    @UiField Anchor moreLessAnchor;


    @SuppressWarnings("LeakingThisInConstructor")
    public FlashPanel() {
        initWidget(BINDER.createAndBindUi(this));
        configOptionalRows();
        setVisibilityOnOptionalRows();
        setMoreOrLessAnchorText();
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private void setMoreOrLessAnchorText() {
        moreLessAnchor.setText(verboseInfo ? "«Show Less»" : "«Show More»");
    }

    private void setVisibilityOnOptionalRows() {
        for (TableRowElement row : optionalRows) {
            UIObject.setVisible(row, verboseInfo);
        }
    }

    @UiHandler("moreLessAnchor")
    public void onFlashMoreLessAnchorClicked(ClickEvent event) {
        verboseInfo = !verboseInfo;
        setVisibilityOnOptionalRows();
        setMoreOrLessAnchorText();
    }

    public void setValue(FlashDataCs flashData) {
        // Our flash plugin implementation fixates the values when the page
        // is loaded, so the equality check below is safe for now.  It's a little
        // problematic, as the screen resolution values can swap when a tablet
        // is rotated.
        if (this.flashData == flashData) {
            return;
        }
        this.flashData = flashData;

        versionLabel.setText(nullToEmpty(flashData.getVersion()));
        manufacturerLabel.setText(nullToEmpty(flashData.getManufacturer()));
        languageLabel.setText(nullToEmpty(flashData.getLanguage()));
        osLabel.setText(nullToEmpty(flashData.getOs()));
        cpuArchLabel.setText(nullToEmpty(flashData.getCpuArchitecture()));
        supports32bitLabel.setText(boolToYesNo(flashData.getSupports32BitProcesses()));
        supports64bitLabel.setText(boolToYesNo(flashData.getSupports64BitProcesses()));
        screenColorLabel.setText(nullToEmpty(flashData.getScreenColor()));
        touchscreenTypeLabel.setText(nullToEmpty(flashData.getTouchscreenType()));
        screenResolutionLabel.setText(widthHeightValue(flashData.getScreenResolutionX(), flashData.getScreenResolutionY()));
        lsoStorageTestLabel.setText(boolToPassFail(flashData.getLsoStorageTest()));
        pixelAspectRatioLabel.setText(nullToEmpty(flashData.getPixelAspectRatio()));
        screenDpiLabel.setText(nullToEmpty(flashData.getScreenDPI()));
        avHardwareDisabledLabel.setText(boolToYesNo(flashData.getAvHardwareDisable()));
        fileReadDisabledLabel.setText(boolToYesNo(flashData.getLocalFileReadDisable()));
        hasPrintingLabel.setText(boolToYesNo(flashData.getHasPrinting()));
        hasAccessibilityLabel.setText(boolToYesNo(flashData.getHasAccessibility()));
        hasAudioLabel.setText(boolToYesNo(flashData.getHasAudio()));
        hasMp3Label.setText(boolToYesNo(flashData.getHasMP3()));
        hasEmbeddedVideoLabel.setText(boolToYesNo(flashData.getHasEmbeddedVideo()));
        hasScreenBroadcastLabel.setText(boolToYesNo(flashData.getHasScreenBroadcast()));
        hasScreenPlaybackLabel.setText(boolToYesNo(flashData.getHasScreenPlayback()));
        hasStreamingAudioLabel.setText(boolToYesNo(flashData.getHasStreamingAudio()));
        hasStreamingVideoLabel.setText(boolToYesNo(flashData.getHasStreamingVideo()));
        hasAudioEncoderLabel.setText(boolToYesNo(flashData.getHasAudioEncoder()));
        hasVideoEncoderLabel.setText(boolToYesNo(flashData.getHasVideoEncoder()));
        hasInputMethodEditorLabel.setText(boolToYesNo(flashData.getHasIME()));
        hasMaxLevelIDCLabel.setText(nullToEmpty(flashData.getMaxLevelIDC()));
        playerTypeLabel.setText(nullToEmpty(flashData.getPlayerType()));
        isDebuggerLabel.setText(boolToYesNo(flashData.getIsDebugger()));
        hasTlsLabel.setText(boolToYesNo(flashData.getHasTLS()));
    }

}
