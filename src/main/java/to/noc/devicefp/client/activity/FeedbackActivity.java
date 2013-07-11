/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import java.util.Set;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import to.noc.devicefp.client.entity.FeedbackProxy;
import to.noc.devicefp.client.jsni.CurrentUser;
import to.noc.devicefp.client.request.FeedbackRequest;
import to.noc.devicefp.client.ui.FeedbackView;
import to.noc.devicefp.shared.AppRequestFactory;

public class FeedbackActivity extends AbstractActivity implements FeedbackView.Delegate {

    private static final Logger log = Logger.getLogger(FeedbackActivity.class.getName());
    private final AppRequestFactory requestFactory;

    @SuppressWarnings("LeakingThisInConstructor")
    public FeedbackActivity(AppRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        FeedbackView.instance().setDelegate(this);
        FeedbackView.instance().enableSending(!CurrentUser.instance().isAnonymous());
    }

    @Override
    public void sendMessage(String message) {
        FeedbackRequest request = requestFactory.feedbackRequest();
        FeedbackProxy feedbackProxy = request.create(FeedbackProxy.class);
        feedbackProxy.setMessage(message);
        request.sendFeedback(feedbackProxy).fire(
                new Receiver<Void>() {
                    @Override
                    public void onSuccess(Void ignore) {
                        FeedbackView.instance().showThankYou();
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        Window.alert(error.getMessage());
                    }

                    @Override
                    public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
                        for (ConstraintViolation<?> vio : violations) {
                            Window.alert("Violation: " + vio.getMessage());
                        }
                    }
                });
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        log.info("Feedback activity started");
        panel.setWidget(FeedbackView.instance());
    }

}
