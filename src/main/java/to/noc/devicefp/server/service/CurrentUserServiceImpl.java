/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import to.noc.devicefp.server.domain.entity.OpenIdUser;
import to.noc.devicefp.server.domain.repository.OpenIdUserRepository;

@Service /* Spring service */
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class CurrentUserServiceImpl implements CurrentUserService {
    private static final Logger log = LoggerFactory.getLogger(CurrentUserServiceImpl.class);

    private OpenIdUser currentUser;

    @Autowired private OpenIdUserRepository userRepository;


    @PostConstruct
    public void loadUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String openId = auth.getName();

        if (!"anonymousUser".equals(openId)) {
            currentUser = userRepository.findByOpenId(openId);
        } else {
            /* Create anonymous user that's never persisted */
            currentUser = new OpenIdUser();
            currentUser.setOpenId(openId);
            currentUser.setRoleName("ROLE_ANONYMOUS");
            currentUser.setFullName("Anonymous User");
        }

        log.debug("CurrentUserService loaded: {}", currentUser.getFullName());
    }


    @Override
    public Long getUserId() {
        return currentUser.getId();
    }

    @Override
    public boolean isAnonymous() {
        return currentUser.isAnonymous();
    }

    @Override
    public boolean isAuthenticated() {
        return !isAnonymous();
    }

    @Override
    public boolean isAdmin() {
        return currentUser.isAdmin();
    }

    @Override
    public String getFullName() {
        return currentUser.getFullName();
    }

    @Override
    public String getEmail() {
        return currentUser.getEmail();
    }
}
