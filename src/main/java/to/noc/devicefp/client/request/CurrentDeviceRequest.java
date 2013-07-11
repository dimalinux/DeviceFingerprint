/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.request;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import java.util.List;
import to.noc.devicefp.client.entity.*;
import to.noc.devicefp.server.service.CurrentDeviceService;
import to.noc.devicefp.server.service.locator.SpringServiceLocator;

@Service(value = CurrentDeviceService.class, locator = SpringServiceLocator.class)
public interface CurrentDeviceRequest extends RequestContext {

    Request<CookieStatesProxy> saveImmediateValues(
                JsDataProxy jsDataProxy,
                DisplayDataProxy displayProxy,
                List<PluginProxy> plugins,
                String existingDeviceStorageCookieId
            );

    Request<Void> saveJavaData(JavaDataProxy javaProxy);

    Request<CookieStatesProxy> saveFlashData(
            FlashDataProxy flashProxy,
            String existingFlashCookieId
    );

    Request<Void> saveBrowserLocation(BrowserLocationProxy location);

    Request<Void> saveReverseGeocode(ReverseGeocodeProxy reverseGeocode);

    Request<CookieStatesProxy> saveSilverlightData(
            SilverlightDataProxy silverlightDataProxy,
            String existingSilverlightCookieId
            );
}
