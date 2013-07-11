/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.request;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import to.noc.devicefp.client.entity.FeedbackProxy;
import to.noc.devicefp.server.service.FeedbackService;
import to.noc.devicefp.server.service.locator.SpringServiceLocator;

@Service(value = FeedbackService.class, locator = SpringServiceLocator.class)
public interface FeedbackRequest extends RequestContext {

    Request<Void> sendFeedback(FeedbackProxy feedbackProxy);

}
