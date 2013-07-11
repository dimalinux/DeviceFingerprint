/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import to.noc.devicefp.shared.AppRequestFactory;


public class AppModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(AppRequestFactory.class).toProvider(RequestFactoryProvider.class).in(Singleton.class);
        bind(PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);
    }

    static class PlaceControllerProvider implements Provider<PlaceController> {
        private final PlaceController placeController;

        @Inject
        public PlaceControllerProvider(EventBus eventBus) {
            this.placeController = new PlaceController(eventBus);
        }

        @Override
        public PlaceController get() {
            return placeController;
        }
    }
    
    private static native String getRequestFactoryUrl() /*-{
        return $wnd.requestFactoryUrl;
    }-*/;
    
    static class RequestFactoryProvider implements Provider<AppRequestFactory> {
        private final AppRequestFactory requestFactory;

        @Inject
        public RequestFactoryProvider(EventBus eventBus) {
            requestFactory = GWT.create(AppRequestFactory.class);
            DefaultRequestTransport requestTransport = new DefaultRequestTransport();
            requestTransport.setRequestUrl(getRequestFactoryUrl());
            requestFactory.initialize(eventBus, requestTransport);
        }

        @Override
        public AppRequestFactory get() {
            return requestFactory;
        }
    }
}
