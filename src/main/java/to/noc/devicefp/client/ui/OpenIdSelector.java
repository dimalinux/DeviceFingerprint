/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * A simple widget which displays info about the user and a logout link.
 */
public class OpenIdSelector {

    interface Binder extends UiBinder<PopupPanel, OpenIdSelector> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField
    DialogBox dialogBox;

    @UiField
    RadioButton googleRadioSelect;

    @UiField
    RadioButton yahooRadioSelect;

    public static native void submitForm() /*-{
        var loginForm = $doc.forms["loginForm"];
        loginForm.action = "/login/openid" + $wnd.location.hash;
        loginForm.submit();
    }-*/;

    @UiHandler("google")
    void handleGoogleClick(ClickEvent e) {
        dialogBox.setText("Redirecting to Google...");
        googleRadioSelect.setValue(true);
        submitForm();
    }

    @UiHandler("yahoo")
    void handleYahooClick(ClickEvent e) {
        dialogBox.setText("Redirecting to Yahoo...");
        yahooRadioSelect.setValue(true);
        submitForm();
    }

    @UiHandler("cancelButton")
    void cancelLogin(ClickEvent e) {
        dialogBox.hide();
    }

    public void show() {
        dialogBox.center();
        dialogBox.show();
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public OpenIdSelector() {
        BINDER.createAndBindUi(this);
    }

}
