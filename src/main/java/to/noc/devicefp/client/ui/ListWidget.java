/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

//
//  Idea came from this blog:
//      https://turbomanage.wordpress.com/2010/02/11/writing-plain-html-in-gwt/
//
public class ListWidget extends ComplexPanel {

    public ListWidget(boolean isOrdered) {
        Document doc = Document.get();
        setElement(isOrdered ? doc.createOLElement() : doc.createULElement());
    }

    public void setId(String id) {
        // Set an attribute common to all tags
        getElement().setId(id);
    }

    public void setDir(String dir) {
        // Set an attribute specific to this tag
        ((UListElement) getElement().cast()).setDir(dir);
    }

    @Override
    public void add(Widget w) {
        // ComplexPanel requires the two-arg add() method
        super.add(w, getElement());
    }
}