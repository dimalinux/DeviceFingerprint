/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.app;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value = {AppModule.class})
public interface AppInjector extends Ginjector {
    public void injectEntryPoint(AppEntryPoint appEntryPoint);
}

