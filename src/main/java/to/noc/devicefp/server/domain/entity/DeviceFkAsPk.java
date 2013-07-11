/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*
 * Device has a bunch of 1<->{0,1} fields where the optional entity's life is
 * tied to the life of the device. This is the common base class for those
 * entities.  We have to encapsulate a device object here for
 * OnDeleteAction.CASCADE to work.
 */
@MappedSuperclass
public abstract class DeviceFkAsPk {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name="device_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Device device;

    public Long getId() {
        return this.id;
    }

    // no setId(), use setDevice() instead

    public Device getDevice() {
        return this.device;
    }

    //
    // setDevice(...) is intentionally package local and only used by Device
    // entities.
    //
    void setDevice(Device device) {
        if (device != null) {
            this.id = device.getId();
        }
        this.device = device;
    }
}
