/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

public interface CurrentUserService {

    Long getUserId();

    boolean isAdmin();

    boolean isAuthenticated();

    boolean isAnonymous();

    String getFullName();

    String getEmail();

    // called once per home page load
    void loadUser();
}
