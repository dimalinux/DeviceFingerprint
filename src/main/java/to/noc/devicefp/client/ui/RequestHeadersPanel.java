/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import java.util.List;
import to.noc.devicefp.client.entity.RequestHeaderCs;

public class RequestHeadersPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);
    
    interface Binder extends UiBinder<SimplePanel, RequestHeadersPanel> {}
    
    @UiField SimplePanel simplePanel;
    
    private List<? extends RequestHeaderCs> requestHeaders;

                    
    @SuppressWarnings("LeakingThisInConstructor")
    public RequestHeadersPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }
            
    public void setValue(List<? extends RequestHeaderCs> requestHeaders) {

        if (this.requestHeaders == requestHeaders) {
            return;
        }
        this.requestHeaders = requestHeaders;
        
        int numRows = (requestHeaders != null) ? requestHeaders.size() : 0;

        if (numRows > 0) {

            Grid grid = new Grid(numRows, 2);

            for (int row = 0; row < numRows; row++) {

                RequestHeaderCs rh = requestHeaders.get(row);

                String name = rh.getName();
                String value = rh.getValue();
                if (name.equalsIgnoreCase("Accept")) {
                    // inserting zero-width spaces after the commas.  Had to
                    // do this as "word-wrap: break-word;" does not work inside
                    // tables with "table-layout: auto;"
                    value = value.replaceAll(",", ",\u200B");
                }
                grid.setWidget(row, 0, new Label(name));
                grid.setWidget(row, 1, new Label(value));
            }
            
            simplePanel.setWidget(grid);
        } else {
            simplePanel.setWidget(new Label("No request headers"));
        }
    }
    
}
