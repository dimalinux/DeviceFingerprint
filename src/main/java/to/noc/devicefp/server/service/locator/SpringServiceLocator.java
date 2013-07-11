/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service.locator;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/*
 * Bridge to access Spring managed objects from GWT's RequestFactoryServlet
 */
public class SpringServiceLocator implements ServiceLocator {

    //private static final Logger log = LoggerFactory.getLogger(SpringServiceLocator.class.getName());

    @Override
    public Object getInstance(Class<?> clazz) {
        return inject(clazz);
    }

    public static Object inject(Class<?> clazz) {
        //log.info("SpringServiceLocator.getInstance({}) called", clazz.toString());
        HttpServletRequest request = RequestFactoryServlet.getThreadLocalRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new IllegalStateException("stale session");
        }
        ServletContext servletContext = session.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return context.getBean(clazz);
    }
}
