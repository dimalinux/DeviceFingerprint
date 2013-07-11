/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The outermost UI of the application.
 */
public class AppShell extends Composite {

    interface Binder extends UiBinder<Widget, AppShell> {}

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField
    SimplePanel masterPanel;

    @UiField
    NotificationMole mole;

    @SuppressWarnings("LeakingThisInConstructor")
    public AppShell() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public SimplePanel getMasterPanel() {
        return masterPanel;
    }

    public NotificationMole getMole() {
        return mole;
    }
}
