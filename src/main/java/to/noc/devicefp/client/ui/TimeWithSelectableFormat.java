/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import java.util.Date;
import to.noc.devicefp.client.entity.JsDataCs;
import to.noc.devicefp.client.ui.TimeFormatSelector.TimeFormat;
import static to.noc.devicefp.client.ui.TimeFormatSelector.TimeFormat.*;
import static to.noc.devicefp.shared.ValDisplayUtil.getIntervalStr;

public class TimeWithSelectableFormat extends Composite implements IsWidget {    
    //private static final Logger log = Logger.getLogger(TimeWithSelectableFormat.class.getName());
    
    private Date time;
    private Date startTime;
    private int viewedUtcOffsetMinutes;
    private TimeFormat currentTimeFormat;
     
    @UiField SpanElement timeDisplay;   
    @UiField Button tzButton;
    
    interface Binder extends UiBinder<Widget, TimeWithSelectableFormat> {}
    private static final Binder BINDER = GWT.create(Binder.class);
   
    @SuppressWarnings("LeakingThisInConstructor")
    public TimeWithSelectableFormat(TimeFormat initialTimeFormat) {                      
        this.currentTimeFormat = initialTimeFormat;
        initWidget(BINDER.createAndBindUi(this));
    }
    
    public void setValue(JsDataCs jsData, Date time) {
        this.time = time;
        startTime = null;
        viewedUtcOffsetMinutes = (jsData != null) ? jsData.getUtcOffsetMin() : 0;
        setTimeLabel();        
    }
    
    public void setValue(JsDataCs jsData, Date time, Date startTime){
        this.time = time;
        this.startTime = startTime;
        viewedUtcOffsetMinutes = (jsData != null) ? jsData.getUtcOffsetMin() : 0;
        setTimeLabel();        
    }        
          
    @Override
    public Widget asWidget() {
        return this;
    }
 
    private TimeFormatSelector.SelectRecipient selectRecipient = new TimeFormatSelector.SelectRecipient() {
        @Override
        public void changeTimeFormat(TimeFormat tz) {
            currentTimeFormat = tz;
            setTimeLabel();
        }

        @Override
        public boolean hasDeltaOption() {
            return startTime != null;
        }

        @Override
        public TimeFormat currentTimeFormat() {
            return currentTimeFormat;
        }

        @Override
        public void setPopupPosition(PopupPanel menuPopup) {
            /* Position evenly on the left side of the button */
            int buttonHeight = tzButton.getOffsetHeight();
            int buttonTop = tzButton.getAbsoluteTop();
            int buttonLeft = tzButton.getAbsoluteLeft();
            
            int popupWidth = menuPopup.getOffsetWidth();
            int popupHeight = menuPopup.getOffsetHeight();
          
            int top = buttonTop + buttonHeight / 2 - popupHeight / 2;
            int left = buttonLeft - popupWidth - 5;
            
            menuPopup.setPopupPosition(left, top);

        }
    }; 
    
    @UiHandler("tzButton")
    void onTzButtonClicked(ClickEvent event) {
        TimeFormatSelector.showPopup(selectRecipient);
    }
    
    private void setTimeLabel() {
        String timeTxt = "-";

        if (time != null) {
            switch (currentTimeFormat) {
                case UTC:
                    timeTxt = DateUtil.formatTime(time, 0);
                    break;
                case DEVICE_TZ:
                    timeTxt = DateUtil.formatTime(time, viewedUtcOffsetMinutes);
                    break;
                case DELTA:
                    timeTxt = getIntervalStr(startTime, time);
                    break;
                case CURRENT_LOCALE:
                    timeTxt = JsDate.create(time.getTime()).toLocaleString();
                    break;
            }
        }
        timeDisplay.setInnerText(timeTxt);        
    }
}
