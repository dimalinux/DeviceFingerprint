/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.auth;

import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import to.noc.devicefp.server.domain.entity.OpenIdUser;
import to.noc.devicefp.server.domain.repository.OpenIdUserRepository;
import to.noc.devicefp.server.service.MailService;

public class AutoRegisteringOpenIdUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    private static final Logger log = LoggerFactory.getLogger(AutoRegisteringOpenIdUserDetailsService.class);

    @Autowired
    private OpenIdUserRepository openIdRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private HttpServletRequest request;


    @Override
    public UserDetails loadUserByUsername(String openId) throws UsernameNotFoundException {
        OpenIdUser user = openIdRepository.findByOpenId(openId);

        if (user == null) {
            log.error("loadUserByUsername called with invalid openId value={}", openId);
            throw new UsernameNotFoundException(openId);
        } else {
            log.debug("loadUserByUsername called with known openId value={}", openId);
        }

        return user;
    }

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) {
        OpenIdUser user;
        String openId = token.getIdentityUrl();

        String mutex = ("oi_lookup_" + openId).intern();
        synchronized (mutex) {
            user = findAndUpdateOrPersistUser(openId, token);
        }

        if (user.getLastLogin() == null) {
            mailService.sendNotificationOfNewUserAsync(user);
        }

        return user;
    }

    @Transactional
    private OpenIdUser findAndUpdateOrPersistUser(String openId, OpenIDAuthenticationToken token) {

        OpenIdUser user = openIdRepository.findByOpenId(openId);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        if (user == null) {
            log.debug("loadUserDetails called on new openid token");
            user = new OpenIdUser();
            user.setOpenId(openId);
            user.setRoleName("ROLE_USER");
            user.setFirstLogin(now);
        } else {
            user.setLastLogin(now);
        }

        user.setRequestHost(request.getHeader("Host"));
        applyTokenAttributesToUser(user, token);

        user = openIdRepository.save(user);

        return user;
    }

    private static void applyTokenAttributesToUser(OpenIdUser user, OpenIDAuthenticationToken token) {
        String firstName = null;
        String lastName = null;
        String fullName = null;

        List<OpenIDAttribute> attributes = token.getAttributes();

        for (OpenIDAttribute attribute : attributes) {
            if (attribute.getName().equals("email")) {
                String email = attribute.getValues().get(0);
                if (!email.isEmpty()) {
                    user.setEmail(email);
                }
            }

            if (attribute.getName().equals("firstname")) {
                firstName = attribute.getValues().get(0);
                if (!firstName.isEmpty()) {
                    user.setFirstName(firstName);
                }
            }

            if (attribute.getName().equals("lastname")) {
                lastName = attribute.getValues().get(0);
                if (!lastName.isEmpty()) {
                    user.setLastName(lastName);
                }
            }

            if (attribute.getName().equals("fullname")) {
                fullName = attribute.getValues().get(0);
                if (!fullName.isEmpty()) {
                    user.setFullName(fullName);
                }
            }
        }

        if (fullName == null) {
            StringBuilder fullNameBldr = new StringBuilder();

            if (firstName != null) {
                fullNameBldr.append(firstName);
            }
            if (lastName != null) {
                fullNameBldr.append(" ").append(lastName);
            }

            if (fullNameBldr.length() > 0) {
                fullName = fullNameBldr.toString();
                user.setFullName(fullName);
            }
        }
    }

}
