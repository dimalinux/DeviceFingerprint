/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import to.noc.devicefp.server.domain.entity.Feedback;

public interface FeedbackService {
    void sendFeedback(Feedback feedback);
}
