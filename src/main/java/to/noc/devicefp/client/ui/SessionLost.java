/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Widget that darkens the page and requests the user to reload after a
 * session is lost.
 */
public class SessionLost {

    interface Binder extends UiBinder<PopupPanel, SessionLost> {
    }
    
    private static final Binder BINDER = GWT.create(Binder.class);
    private static SessionLost instance;
    
    @UiField
    DialogBox dialogBox;

    @SuppressWarnings("LeakingThisInConstructor")
    private SessionLost() {
        BINDER.createAndBindUi(this);
    }

    public static void show() {
        if (instance == null) {
            instance = new SessionLost();
        }
        instance.showDialog();
    }

    private void showDialog() {
        dialogBox.center();
        dialogBox.show();
    }

    @UiHandler("reload")
    void reloadClicked(ClickEvent e) {
        dialogBox.setText("... Reloading ...");
        Window.Location.reload();
    }
}
