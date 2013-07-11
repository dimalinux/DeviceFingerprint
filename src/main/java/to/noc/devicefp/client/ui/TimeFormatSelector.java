/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/* 
 * There should never be more than one TimeFormat popup on the screen at a
 * given time, so I made this a singleton.
 */
public class TimeFormatSelector {    
      
    public static enum  TimeFormat { UTC, DEVICE_TZ, CURRENT_LOCALE, DELTA } ;

    interface Binder extends UiBinder<PopupPanel, TimeFormatSelector> {}
    private static final Binder BINDER = GWT.create(Binder.class);
    
    interface Style extends CssResource {
        String menuItem();
        String selectedMenuItem();
    }
    @UiField Style style;
        
    private static TimeFormatSelector instance;
    private static SelectRecipient selectRecipient;
   
    @UiField PopupPanel popupPanel;
        
    @UiField Label utcSelect;
    @UiField Label deviceTzSelect;
    @UiField Label currentLocaleSelect;
    @UiField Label deltaSelect;

    public interface SelectRecipient {
        void changeTimeFormat(TimeFormat tz);
        boolean hasDeltaOption();
        TimeFormat currentTimeFormat();
        void setPopupPosition(PopupPanel menuPopup);
    }
    
    @SuppressWarnings("LeakingThisInConstructor")       
    private TimeFormatSelector() {
        BINDER.createAndBindUi(this);
    }
    
    public static void showPopup(SelectRecipient selectRecipient) {
        TimeFormatSelector.selectRecipient = selectRecipient;
        if (instance == null) {
            instance = new TimeFormatSelector();
        }
        instance.showPopup();
    }
    
    private void showPopup() {
        deltaSelect.setVisible(selectRecipient.hasDeltaOption());

        TimeFormat currentFormat = selectRecipient.currentTimeFormat();
        for (TimeFormat fmt : TimeFormat.values()) {
            Label label = getMenuLabel(fmt);
            if (fmt != currentFormat) {
                label.removeStyleName(style.selectedMenuItem());
            } else {
                label.addStyleName(style.selectedMenuItem());
            }
        }
        
        popupPanel.setVisible(false);
        popupPanel.show();

        // The first time we display the popup, CSS has not been applied and
        // the sizing is inaccurate. Pausing display until the event loop is
        // free allows us to compute the correct position.
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                selectRecipient.setPopupPosition(popupPanel);
                popupPanel.setVisible(true);
            }
        });
    
    }
    
    private Label getMenuLabel(TimeFormat fmt) {
        switch (fmt) {
            case UTC:
                return utcSelect;
            case DEVICE_TZ:
                return deviceTzSelect;
            case CURRENT_LOCALE:
                return currentLocaleSelect;
            case DELTA:
            default:
                return deltaSelect;
        }
    }
      
    private void select(TimeFormat tz) {
        if (tz != selectRecipient.currentTimeFormat()) {
            selectRecipient.changeTimeFormat(tz);
        }
        popupPanel.hide();
    }

    @UiHandler("utcSelect")
    void onUtcSelectClicked(ClickEvent event) {
        select(TimeFormat.UTC);
    }

    @UiHandler("deviceTzSelect")
    void onDeviceTzSelectClicked(ClickEvent event) {
        select(TimeFormat.DEVICE_TZ);
    }

    @UiHandler("currentLocaleSelect")
    void onCurrentLocaleSelectClicked(ClickEvent event) {
        select(TimeFormat.CURRENT_LOCALE);
    }

    @UiHandler("deltaSelect")
    void onDeltaSelectClicked(ClickEvent event) {
        select(TimeFormat.DELTA);
    }
  
}
