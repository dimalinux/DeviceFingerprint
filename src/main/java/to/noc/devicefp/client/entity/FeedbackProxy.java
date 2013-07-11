/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import to.noc.devicefp.server.domain.entity.Feedback;
import to.noc.devicefp.server.service.locator.FeedbackLocator;

@ProxyFor(value = Feedback.class, locator = FeedbackLocator.class)
public interface FeedbackProxy extends EntityProxy {
    Long getId();

    String getMessage();
    void setMessage(String message);
}
