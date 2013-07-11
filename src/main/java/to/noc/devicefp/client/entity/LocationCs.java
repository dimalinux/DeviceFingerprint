/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

import java.util.Date;

/* Client-side (Cs) interface for classes holding Location data */
public interface LocationCs {
    public Date getStamp();
    public Double getLatitude();
    public Double getLongitude();
    public Integer getAccuracyRadius();
}
