/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import java.util.List;
import to.noc.devicefp.client.entity.CookieStatesCs;
import to.noc.devicefp.client.entity.JsDataCs;
import to.noc.devicefp.client.entity.RequestHeaderCs;
import to.noc.devicefp.client.entity.ZombieCookieCs;
import static to.noc.devicefp.client.ui.TimeFormatSelector.TimeFormat.DEVICE_TZ;
import to.noc.devicefp.shared.CookieState;
import static to.noc.devicefp.shared.CookieState.*;

public class ZombieCookiePanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, ZombieCookiePanel> {}

    @UiField Label idLabel;

    @UiField(provided=true) TimeWithSelectableFormat inceptionTime = new TimeWithSelectableFormat(DEVICE_TZ);

    @UiField Element regularCookieRow;
    @UiField Label regularCookieLabel;

    @UiField Element etagRequestCookieRow;
    @UiField Label etagRequestCookieLabel;

    @UiField Element etagResponseCookieRow;
    @UiField Label etagResponseCookieLabel;

    @UiField Element webStorageCookieRow;
    @UiField Label webStorageCookieLabel;

    @UiField Element flashCookieRow;
    @UiField Label flashCookieLabel;

    @UiField Element silerlightCookieRow;
    @UiField Label silerlightCookieLabel;


    @SuppressWarnings("LeakingThisInConstructor")
    public ZombieCookiePanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private void setCookieState(Element row, Label label, CookieState state) {
        boolean visible = false;
        if (state != null) {
            visible = true;
            String txt;
            if (row == etagResponseCookieRow && state == REPLACED_BY_OLDER_ETAG_COOKIE) {
                txt = "set from older cookie linked to etag request";
            } else {
                txt = state.toString().replace("_", " ").toLowerCase();
            }
            label.setText(txt);
        }
        UIObject.setVisible(row, visible);
    }

    private void setEtagRequestState(CookieStatesCs states, List<? extends RequestHeaderCs> requestHeaders) {
        // If the response state is null, the device was probably created before
        // we started using etags. The request state here is computed programatically
        // from the headers.
        boolean visible = (states.getEtagState() != null);
        if (visible) {
            CookieState plainState = states.getPlainState();
            String state = "not present";

            if (findIfNoneMatchRequestHeader(requestHeaders)) {
                state = "present";
            } else if (plainState == EXISTING || plainState.isReplacedByOlderState()
                    || findNoCacheRequestHeader(requestHeaders)) {
                state += ", some browsers skip on current page refresh";
            }
            etagRequestCookieLabel.setText(state);
        }

        UIObject.setVisible(etagRequestCookieRow, visible);
    }

    private static boolean findIfNoneMatchRequestHeader(List<? extends RequestHeaderCs> requestHeaders) {
        if (requestHeaders != null) {
            for (RequestHeaderCs rh : requestHeaders) {
                if ("If-None-Match".equals(rh.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean findNoCacheRequestHeader(List<? extends RequestHeaderCs> requestHeaders) {
        if (requestHeaders != null) {
            for (RequestHeaderCs rh : requestHeaders) {
                if ("Cache-Control".equals(rh.getName())) {
                    String value = rh.getValue();
                    if (value != null && value.toLowerCase().matches(".*max-age=0|no-cache.*")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setValue(
            JsDataCs jsData,
            CookieStatesCs cookieStates,
            List<? extends RequestHeaderCs> requestHeaders
            ) {

        ZombieCookieCs zombieCookie = cookieStates.getZombieCookie();

        idLabel.setText(zombieCookie.getId());
        inceptionTime.setValue(jsData, zombieCookie.getInception());

        setCookieState(
                regularCookieRow,
                regularCookieLabel,
                cookieStates.getPlainState()
                );
        setEtagRequestState(cookieStates, requestHeaders);
        setCookieState(
                etagResponseCookieRow,
                etagResponseCookieLabel,
                cookieStates.getEtagState()
                );
        setCookieState(
                webStorageCookieRow,
                webStorageCookieLabel,
                cookieStates.getWebStorageState()
                );
        setCookieState(
                flashCookieRow,
                flashCookieLabel,
                cookieStates.getFlashState()
                );
        setCookieState(
                silerlightCookieRow,
                silerlightCookieLabel,
                cookieStates.getSilverlightState()
                );
    }

}
