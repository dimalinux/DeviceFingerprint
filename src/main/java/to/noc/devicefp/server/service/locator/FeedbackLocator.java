/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.Service;
import to.noc.devicefp.server.domain.entity.Feedback;
import to.noc.devicefp.server.service.FeedbackService;

@Service(value=FeedbackService.class) // GWT service, not Spring
public class FeedbackLocator extends Locator<Feedback, Long> {

    @Override
    public Feedback create(Class<? extends Feedback> clazz) {
        return new Feedback();
    }

    @Override
    public Feedback find(Class<? extends Feedback> clazz, Long id) {
        // Never called by the RequestFactoryServlet or client
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<Feedback> getDomainType() {
        return Feedback.class;
    }

    @Override
    public Long getId(Feedback feedback) {
        return feedback.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return Long.class;
    }

    @Override
    public Object getVersion(Feedback feedback) {
        return feedback.getVersion();
    }

    @Override
    public boolean isLive(Feedback feedback) {
        // We don't ever delete
        return true;
    }

}
