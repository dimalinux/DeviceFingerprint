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
import com.google.gwt.user.client.ui.*;


public class FeedbackView extends Composite  {
    //private static final Logger log = Logger.getLogger(FeedbackView.class.getName());

    public interface Delegate {
        public void sendMessage(String message);
    }

    interface Binder extends UiBinder<HTMLPanel, FeedbackView> {}
    private static final Binder BINDER = GWT.create(Binder.class);

    private static FeedbackView instance;

    @UiField
    TextArea feedbackArea;

    @UiField
    Button send;

    private Delegate delegate;

    @SuppressWarnings("LeakingThisInConstructor")
    private FeedbackView() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public static FeedbackView instance() {
        if (instance == null) {
            instance = new FeedbackView();
        }
        return instance;
    }

    public void enableSending(boolean enabled) {
        send.setEnabled(enabled);
        String startMessage = enabled ? "" : "Use the OpenID login button to leave feedback.";
        feedbackArea.setText(startMessage);
        feedbackArea.setEnabled(enabled);
    }

    public void showThankYou() {
        // TBD: should not use an "alert" message style
        Window.alert("Thanks!  Message sent.");
        feedbackArea.setText("");
    }


    @UiHandler("send")
    public void onSendClicked(ClickEvent e) {
        send.setEnabled(false);
        // Ensure that the markup stripped version has non-whitespace in it
        // TBD: still broken (non-empty white space message still send)
        if (feedbackArea.getText().replaceAll("\\s+", "").length() > 0) {
            delegate.sendMessage(feedbackArea.getText());
        } else {
            Window.alert("You forgot to write a message!");
        }
        send.setEnabled(true);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

}
