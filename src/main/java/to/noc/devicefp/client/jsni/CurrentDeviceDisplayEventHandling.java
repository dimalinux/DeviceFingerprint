/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import java.util.logging.Logger;
import to.noc.devicefp.client.ui.DeviceDetailsView;

public class CurrentDeviceDisplayEventHandling {

    private static final Logger log = Logger.getLogger(CurrentDeviceDisplayEventHandling.class.getName());
    private final CurrentDisplayData displayData = CurrentDisplayData.instance();
    private final DeviceDetailsView deviceDetailsView = DeviceDetailsView.instance();
    private boolean isRunning = false;

    private static CurrentDeviceDisplayEventHandling instance;

    // enforce singleton
    private CurrentDeviceDisplayEventHandling() {}

    public static CurrentDeviceDisplayEventHandling instance() {
        if (instance == null) {
            instance = new CurrentDeviceDisplayEventHandling();
        }
        return instance;
    }

    private ResizeHandler windowResizeHandler = new ResizeHandler() {
        @Override
        public void onResize(ResizeEvent event) {
            refreshScreenDimensions();
        }
    };

    private Window.ScrollHandler windowScrollHandler = new Window.ScrollHandler() {
        @Override
        public void onWindowScroll(ScrollEvent event) {
            refreshScreenDimensions();
        }
    };

    private Event.NativePreviewHandler nativePreviewHandler = new Event.NativePreviewHandler() {
        private double lastTouchEventMs = 0;

        @Override
        public void onPreviewNativeEvent(NativePreviewEvent previewEvent) {
            try {
                NativeEvent event = previewEvent.getNativeEvent();
                int type = previewEvent.getTypeInt();

                // Touch events are duplicated as mouse events on many (all?)
                // browsers.  To make mobile devices more responsive and deal
                // with bad screen[xy] values on the duplicated events from mobile
                // chrome, we ignore mouse events for 1 second after a touch
                // event.
                double nowMs = JsDate.create().getTime();
                if ((type & Event.MOUSEEVENTS) != 0
                        && (nowMs - lastTouchEventMs > 1000)
                        && displayData.setLastEvent(event)) {
                    refreshScreenDimensions();
                } else if ((type & Event.TOUCHEVENTS) != 0) {
                    JsArray<Touch> touches = event.getTouches();
                    if (touches != null && touches.length() > 0) {
                        lastTouchEventMs = JsDate.create().getTime();
                        if (displayData.setLastEvent(touches.get(0))) {
                            refreshScreenDimensions();
                        }
                    }
                }
            } catch (Exception ex) {
                log.warning(ex.toString());
            }
        }
    };
    private boolean refreshPending = false;

    private void refreshScreenDimensions() {
        if (!refreshPending) {
            refreshPending = true;
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    deviceDetailsView.refreshDisplayData();
                    refreshPending = false;
                }
            });
        }
    }
    //
    // There is no event for detecting browser movement on the outer
    // display, so I added a periodic refresh timer.
    //
    private Timer refreshTimer = new Timer() {
        @Override
        public void run() {
            refreshScreenDimensions();
        }
    };
    HandlerRegistration windowResizeRegistration;
    HandlerRegistration windowScrollRegistration;
    HandlerRegistration nativePreviewRegistratioin;

    public void startEventHandling() {
        if (!isRunning) {
            windowResizeRegistration = Window.addResizeHandler(windowResizeHandler);
            windowScrollRegistration = Window.addWindowScrollHandler(windowScrollHandler);
            nativePreviewRegistratioin = Event.addNativePreviewHandler(nativePreviewHandler);
            refreshTimer.scheduleRepeating(1000);
            isRunning = true;
        }
    }

    public void stopEventHandling() {
        if (isRunning) {
            windowResizeRegistration.removeHandler();
            windowScrollRegistration.removeHandler();
            nativePreviewRegistratioin.removeHandler();
            refreshTimer.cancel();
            isRunning = false;
        }
    }
}