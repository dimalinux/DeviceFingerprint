/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import to.noc.devicefp.server.domain.entity.SilverlightData;

@ProxyFor(value = SilverlightData.class)
public interface SilverlightDataProxy extends ValueProxy, SilverlightDataCs {
    public void setOsVersion(String osVersion);
    public void setClrVersion(String clrVersion);
    public void setAssemblyClrVersion(String assemblyClrVersion);
    public void setProcessorCount(Integer processorCount);
    public void setSysUptimeMs(Integer sysUptimeMs);
    public void setIsolatedStorageEnabled(Boolean isolatedStorageEnabled);
    public void setIsolatedStorageTest(Boolean isolatedStorageTest);
}
