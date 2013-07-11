/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.app;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import to.noc.devicefp.client.activity.AppActivityMapper;
import to.noc.devicefp.client.place.AppPlaceHistoryMapper;
import to.noc.devicefp.client.request.RequestEvent;
import to.noc.devicefp.client.ui.AppShell;
import to.noc.devicefp.client.ui.LoginButton;
import to.noc.devicefp.client.ui.MainMenu;
import to.noc.devicefp.client.ui.SessionLost;
import to.noc.devicefp.shared.AppRequestFactory;

public class AppEntryPoint  implements EntryPoint {
    // this value should be shorter than the server session timeout in web.xml
    private static int SESSION_KEEPALIVE_INTERVAL = 1000*60*20; /* 20 mins */

    @Inject private AppRequestFactory requestFactory;
    @Inject private EventBus eventBus;
    @Inject private PlaceController placeController;

    private AppShell shell;
    private MainMenu mainMenu;

    @SuppressWarnings("LeakingThisInConstructor")
    public AppEntryPoint() {
        AppInjector injector = GWT.create(AppInjector.class);
        injector.injectEntryPoint(this);
        mainMenu = new MainMenu();
        eventBus.addHandler(PlaceChangeEvent.TYPE, mainMenu);
        shell = new AppShell();
    }

    @Override
    public void onModuleLoad() {
        /* Add handlers, setup activities */
        init();

        RootPanel.get("gwtStart").add(shell);
        LoginButton.init(RootPanel.get("loginLogoutButton").getElement());

        /* Hide the loading image */
        Element loading = Document.get().getElementById("loadingImg");
        loading.getParentElement().removeChild(loading);
    }

    private void init() {
        initLogging();
        //final Logger log = Logger.getLogger(AppEntryPoint.class.getName());

        GWT.setUncaughtExceptionHandler(new AppUncaughtExceptionHandler());

        RequestEvent.register(eventBus, new RequestEvent.Handler() {
            // Only show loading status if a request isn't serviced in 250ms.
            private static final int LOADING_TIMEOUT = 250;

            @Override
            public void onRequestEvent(RequestEvent requestEvent) {
                if (requestEvent.getState() == RequestEvent.State.SENT) {
                    shell.getMole().showDelayed(LOADING_TIMEOUT);
                } else {
                    shell.getMole().hide();
                }
            }
        });

        ActivityMapper activityMapper = new AppActivityMapper(requestFactory);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);

        activityManager.setDisplay(shell.getMasterPanel());

        /* Browser history integration */
        AppPlaceHistoryMapper historyMapper = new AppPlaceHistoryMapper();
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, MainMenu.getDefaultPlace());
        historyHandler.handleCurrentHistory();

        initWindowCloseHandler();
        initSessionKeepAlive();
    }

    private void initLogging() {
        if (LogConfiguration.loggingIsEnabled()) {
            // Add remote logging handler
            RequestFactoryLogHandler.LoggingRequestProvider provider =
                    new RequestFactoryLogHandler.LoggingRequestProvider() {
                        @Override
                        public LoggingRequest getLoggingRequest() {
                            return requestFactory.loggingRequest();
                        }
                    };
            Logger.getLogger("").addHandler(
                    new RequestFactoryLogHandler(provider, Level.WARNING, new ArrayList<String>())
                    );
        }

    }

    /**
     * This was the best solution I could think of to avoid session timeouts on
     * an open window, but I'm open to better ideas.
     */
    private void initSessionKeepAlive() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                requestFactory.sessionRequest().keepSessionAlive().fire(
                        new Receiver<Boolean>() {
                            private void  showError() {
                                SessionLost.show();
                            }

                            @Override
                            public void onSuccess(Boolean isAlive) {
                                if (isAlive != Boolean.TRUE) {
                                    cancel();
                                    showError();
                                }
                            }

                            @Override
                            public void onFailure(ServerFailure error) {
                                cancel();
                                showError();
                            }
                        });
            }
        };

        timer.scheduleRepeating(SESSION_KEEPALIVE_INTERVAL);
    }

    private void initWindowCloseHandler() {
        Window.addCloseHandler(new CloseHandler<Window>() {
            @Override
            public void onClose(CloseEvent<Window> event) {
                requestFactory.sessionRequest().windowClosed(new Date()).fire(
                    new Receiver<Void>() {
                        @Override
                        public void onSuccess(Void response) {
                        }

                        @Override
                        public void onFailure(ServerFailure error) {
                        }
                });
            }
        });
    }

}
