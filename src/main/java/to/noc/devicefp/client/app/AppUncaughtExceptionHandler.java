/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.app;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.UmbrellaException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppUncaughtExceptionHandler implements UncaughtExceptionHandler {
    static final Logger log = Logger.getLogger(AppUncaughtExceptionHandler.class.getName());
    private final int MIN_DELAY_BETWEEN_ERROR_POPUPS = 60 * 1000; /* 1 min */

    private Duration elapsedTime = null;

    @Override
    public void onUncaughtException(Throwable e) {
        Throwable exceptionToDisplay = getExceptionToDisplay(e);

        if (elapsedTime == null
                || elapsedTime.elapsedMillis() > MIN_DELAY_BETWEEN_ERROR_POPUPS) {
            Window.alert(exceptionToDisplay.getMessage());
            elapsedTime = new Duration();
            log.log(Level.SEVERE, exceptionToDisplay.getMessage(), exceptionToDisplay);
        }


    }

    //
    // Get rid of UmbrellaException
    // Idea came from this blog:
    // http://cleancodematters.com/2011/05/29/improved-exceptionhandling-with-gwts-requestfactory/
    //
    private static Throwable getExceptionToDisplay(Throwable throwable) {
        Throwable result = throwable;
        if (throwable instanceof UmbrellaException && ((UmbrellaException) throwable).getCauses().size() == 1) {
            result = ((UmbrellaException) throwable).getCauses().iterator().next();
        }
        return result;
    }
}