/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.shared;

import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import to.noc.devicefp.client.request.CurrentDeviceRequest;
import to.noc.devicefp.client.request.FeedbackRequest;
import to.noc.devicefp.client.request.SavedDeviceRequest;
import to.noc.devicefp.client.request.SessionRequest;

public interface AppRequestFactory extends RequestFactory  {
    public SavedDeviceRequest deviceRequest();
    public LoggingRequest loggingRequest();
    public SessionRequest sessionRequest();
    public FeedbackRequest feedbackRequest();
    public CurrentDeviceRequest currentDeviceRequest();
}
