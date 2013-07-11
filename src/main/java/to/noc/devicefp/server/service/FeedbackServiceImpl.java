/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import to.noc.devicefp.server.domain.entity.Feedback;
import to.noc.devicefp.server.domain.repository.FeedbackRepository;
import to.noc.devicefp.server.domain.repository.OpenIdUserRepository;

@Service /* Spring service */
public class FeedbackServiceImpl implements FeedbackService {
    private static final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    @Autowired private FeedbackRepository feedbackRepository;
    @Autowired private CurrentUserService currentUserService;
    @Autowired private OpenIdUserRepository userRepository;
    @Autowired private MailService mailService;


    @Override
    public void sendFeedback(Feedback feedback) {
        feedback = persistFeedback(feedback);
        mailService.sendFeedbackAsync(feedback);
    }

    @Transactional
    private Feedback persistFeedback(Feedback feedback) {

        if (!currentUserService.isAuthenticated()) {
            log.error("aborting feedback operation from unathenticated user");
            throw new AccessDeniedException("must be authenticated");
        }

        feedback = feedbackRepository.save(feedback); // create managed entity
        feedback.setUser(userRepository.findOne(currentUserService.getUserId()));

        return feedback;
    }

}
