/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import org.springframework.scheduling.annotation.Async;
import to.noc.devicefp.server.domain.entity.Feedback;
import to.noc.devicefp.server.domain.entity.OpenIdUser;


public interface MailService {

    boolean sendMail(String cc, String subject, String messageBody);

    @Async
    void sendFeedbackAsync(Feedback feedback);

    @Async
    void sendNotificationOfNewUserAsync(OpenIdUser oidUser);

}
