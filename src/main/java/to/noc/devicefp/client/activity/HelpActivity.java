/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import java.util.logging.Logger;
import to.noc.devicefp.client.ui.HelpView;

public class HelpActivity extends AbstractActivity {

    private static final Logger log = Logger.getLogger(HelpActivity.class.getName());
    
    public HelpActivity() {
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        log.fine("Help activity started");
        panel.setWidget(HelpView.instance());
    }
}
