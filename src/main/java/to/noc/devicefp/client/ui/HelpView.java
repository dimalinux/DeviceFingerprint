/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;


public class HelpView extends Composite  {

    interface Binder extends UiBinder<HTML, HelpView> {}
    private static final Binder BINDER = GWT.create(Binder.class);
    private static HelpView instance;       
        
    @SuppressWarnings("LeakingThisInConstructor")
    private HelpView() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public static HelpView instance() {
        if (instance == null) {
            instance = new HelpView();
        }
        return instance;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
   
    @Override
    protected void onLoad() {
        // if the user does a direct page load on a fragment subsection of the
        // help, the browser won't have scrolled down to the requested subsection
        // as this widget hasn't loaded yet, so we reset the location.
        if (Window.Location.getHash().matches("#.*:\\w+")) {
            scroll();
        }
    }

    public static native void scroll() /*-{
        var temp = $wnd.location.hash;
        $wnd.location.hash = '';
        $wnd.location.hash = temp;
    }-*/;
        
    
}
