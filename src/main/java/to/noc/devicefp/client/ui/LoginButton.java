/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import to.noc.devicefp.client.jsni.CurrentUser;
import to.noc.devicefp.client.jsni.NavigatorFull;

/**
 * Wraps an existing login button and provides click behavior.
 */
public final class LoginButton {

    private static OpenIdSelector  oidSelector;
    private static boolean isAuthenticated = false;

    private static ClickHandler clickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
            if (!isAuthenticated) {
                if (oidSelector == null) {
                    oidSelector = new OpenIdSelector();
                }
                if (NavigatorFull.getCookiesEnabled() == true) {
                    oidSelector.show();
                } else {
                    Window.alert("Enable cookies and reload page for login");
                }
            } else {
                String logoutUrl = "/logout" + Window.Location.getHash();
                Window.open(logoutUrl, "_self", "");
            }
        }
    };

    public static void init(Element buttonToWrap) {
        Button loginLogoutButton = Button.wrap(buttonToWrap);

        isAuthenticated = !CurrentUser.instance().isAnonymous();
        loginLogoutButton.addClickHandler(clickHandler);
        loginLogoutButton.setEnabled(true);
    }

    private LoginButton() {}
}
