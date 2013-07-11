/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import to.noc.devicefp.client.entity.DisplayDataCs;
import to.noc.devicefp.client.jsni.CurrentDisplayData;
import static to.noc.devicefp.shared.ValDisplayUtil.*;

public class DisplayPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTML, DisplayPanel> {}

    @UiField SpanElement screenWidthHeightSpan;

    @UiField SpanElement availWidthHeightSpan;

    @UiField Element windowOuterWidthHeightRow;
    @UiField SpanElement windowOuterWidthHeightSpan;

    @UiField Element windowInnerWidthHeightRow;
    @UiField SpanElement windowInnerWidthHeightSpan;

    @UiField Element clientWidthHeightRow;
    @UiField SpanElement clientWidthHeightSpan;

    @UiField Element windowPositionRow;
    @UiField SpanElement windowPositionSpan;

    @UiField Element pageOffsetPositionRow;
    @UiField SpanElement pageOffsetPositionSpan;

    @UiField Element offsetWidthHeightRow;
    @UiField SpanElement offsetWidthHeightSpan;

    @UiField Element mouseScreenPositionRow;
    @UiField SpanElement mouseScreenPositionSpan;

    @UiField Element mouseClientPositionRow;
    @UiField SpanElement mouseClientPositionSpan;

    @UiField Element mousePagePositionRow;
    @UiField SpanElement mousePagePositionSpan;

    @UiField Element colorDepthRow;
    @UiField SpanElement colorDepthSpan;

    @UiField Element pixelDepthRow;
    @UiField SpanElement pixelDepthSpan;

    @UiField Element pixelRatioRow;
    @UiField SpanElement pixelRatioSpan;

    @UiField Element fontSmoothingRow;
    @UiField SpanElement fontSmoothingSpan;

    @UiField Element bufferDepthRow;
    @UiField SpanElement bufferDepthSpan;

    @UiField Element deviceDPIRow;
    @UiField SpanElement deviceDPISpan;

    @UiField Element logicalDPIRow;
    @UiField SpanElement logicalDPISpan;

    @UiField Element systemDPIRow;
    @UiField SpanElement systemDPISpan;

    @UiField Element updateIntervalRow;
    @UiField SpanElement updateIntervalSpan;

    @SuppressWarnings("LeakingThisInConstructor")
    private DisplayPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private static void setOptionalWidthHeight(Element row, SpanElement valueSpan, Integer width, Integer height) {
        boolean visible = false;
        if (width != null || height != null) {
            visible = true;
            valueSpan.setInnerText(widthHeightValue(width, height));
        }
        UIObject.setVisible(row, visible);
    }

    private static void setOptionalCoordinate(Element row, SpanElement valueSpan, Integer xPos, Integer yPos) {
        boolean visible = false;
        if (xPos != null || yPos != null) {
            visible = true;
            valueSpan.setInnerText(coordinateValue(xPos, yPos, false));
        }
        UIObject.setVisible(row, visible);
    }

    private static void setOptionalValue(Element row, SpanElement valueSpan, Object value) {
        boolean visible = false;
        if (value != null) {
            visible = true;
            valueSpan.setInnerText(value.toString());
        }
        UIObject.setVisible(row, visible);
    }


    public void setValue(DisplayDataCs displayData, boolean displayMouseValues) {

        screenWidthHeightSpan.setInnerText(widthHeightValue(displayData.getWidth(), displayData.getHeight()));
        availWidthHeightSpan.setInnerText(widthHeightValue(displayData.getAvailWidth(), displayData.getAvailHeight()));

        windowPositionSpan.setInnerText(coordinateValue(
                    displayData.getScreenX(),
                    displayData.getScreenY(),
                    false
                ));

        setOptionalWidthHeight(
                windowOuterWidthHeightRow, windowOuterWidthHeightSpan,
                displayData.getOuterWidth(), displayData.getOuterHeight()
                );

        setOptionalWidthHeight(
                windowInnerWidthHeightRow, windowInnerWidthHeightSpan,
                displayData.getInnerWidth(), displayData.getInnerHeight()
                );

        setOptionalWidthHeight(
                clientWidthHeightRow, clientWidthHeightSpan,
                displayData.getClientWidth(), displayData.getClientHeight()
                );

        setOptionalCoordinate(
                pageOffsetPositionRow, pageOffsetPositionSpan,
                displayData.getPageXOffset(), displayData.getPageYOffset()
                );

        setOptionalWidthHeight(
                offsetWidthHeightRow, offsetWidthHeightSpan,
                displayData.getOffsetWidth(), displayData.getOffsetHeight()
                );


        Integer colorDepth = displayData.getColorDepth();
        colorDepthSpan.setInnerText(nullToEmpty(colorDepth));

        boolean pixelDepthVisible = false;
        Integer pixelDepth = displayData.getPixelDepth();
        if (pixelDepth != null && !pixelDepth.equals(colorDepth)) {
            pixelDepthVisible = true;
            pixelDepthSpan.setInnerText(pixelDepth.toString());
        }
        UIObject.setVisible(pixelDepthRow, pixelDepthVisible);

        setOptionalValue(pixelRatioRow, pixelRatioSpan, displayData.getPixelRatio());


        Boolean fontSmoothing = displayData.getFontSmoothingEnabled();
        boolean fontSmoothingVisible = fontSmoothing != null;
        if (fontSmoothingVisible) {
            fontSmoothingSpan.setInnerText(boolToEnabled(fontSmoothing));
        }
        UIObject.setVisible(fontSmoothingRow, fontSmoothingVisible);

        setOptionalValue(bufferDepthRow, bufferDepthSpan, displayData.getBufferDepth());

        setOptionalCoordinate(
                deviceDPIRow, deviceDPISpan,
                displayData.getDeviceXDPI(), displayData.getDeviceYDPI()
                );

        setOptionalCoordinate(
                logicalDPIRow, logicalDPISpan,
                displayData.getLogicalXDPI(), displayData.getLogicalYDPI()
                );

        setOptionalCoordinate(
                systemDPIRow, systemDPISpan,
                displayData.getSystemXDPI(), displayData.getSystemYDPI()
                );

        setOptionalValue(updateIntervalRow, updateIntervalSpan, displayData.getUpdateInterval());


        if (displayMouseValues) {
            CurrentDisplayData curDisplayData = (CurrentDisplayData) displayData;

            setOptionalCoordinate(
                    mouseScreenPositionRow, mouseScreenPositionSpan,
                    curDisplayData.getMouseScreenX(), curDisplayData.getMouseScreenY()
                    );

            setOptionalCoordinate(
                    mouseClientPositionRow, mouseClientPositionSpan,
                    curDisplayData.getMouseClientX(), curDisplayData.getMouseClientY()
                    );

            setOptionalCoordinate(
                    mousePagePositionRow, mousePagePositionSpan,
                    curDisplayData.getMousePageX(), curDisplayData.getMousePageY()
                    );
        } else {
            UIObject.setVisible(mouseScreenPositionRow, false);
            UIObject.setVisible(mouseClientPositionRow, false);
            UIObject.setVisible(mousePagePositionRow, false);
        }

    }

}
