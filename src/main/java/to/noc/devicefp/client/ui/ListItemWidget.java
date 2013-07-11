/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ListItemWidget extends SimplePanel {

    public ListItemWidget() {
        super((Element) Document.get().createLIElement().cast());
    }

    public ListItemWidget(String s) {
        this();
        getElement().setInnerText(s);
    }

    public ListItemWidget(Widget w) {
        this();
        this.add(w);
    }
}
