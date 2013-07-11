/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.gwt;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static to.noc.devicefp.server.util.IpUtil.ipWithHostName;

/*
 * We're using a customized RequestFactoryServlet so we can have our own
 * ExceptionHandler that logs and does pass the stack trace to the client.
 *
 * Idea came from this blog:
 *  http://cleancodematters.com/2011/05/29/improved-exceptionhandling-with-gwts-requestfactory/
 *
 */
public class DeviceFingerprintRequestFactoryServlet extends RequestFactoryServlet {

    static class LoggingExceptionHandler extends DefaultExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(LoggingExceptionHandler.class);

        @Override
        public ServerFailure createServerFailure(Throwable throwable) {

            StringBuilder message = new StringBuilder("Server error");
            HttpServletRequest request = getThreadLocalRequest();
            if (request != null) {
                message.append(" requestUrl=").append(request.getRequestURL());
                message.append(" remoteIp=").append(ipWithHostName(request.getRemoteAddr()));
            }
            log.error(message.toString(), throwable);
            return new ServerFailure(
                    throwable.getMessage(),
                    throwable.getClass().getName(),
                    null,  // stack trace string (don't give to client)
                    true   // is fatal
                    );
        }
    }

    public DeviceFingerprintRequestFactoryServlet() {
        super(new LoggingExceptionHandler());
    }
}
