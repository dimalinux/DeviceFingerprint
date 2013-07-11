/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="headers")
public class RequestHeader implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional=false)
    @ForeignKey(name="fk_reqhead_to_dev")
    @JoinColumn(name="device_id")
    protected Device device;

    @Size(max=128)
    @Column(length=128)
    private String name;

    @Size(max=2048)
    @Column(name="val", length=2048)
    private String value;

    public RequestHeader() {}

    public RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getDevice() {
        return this.device;
    }

    // setDevice(...) intentionally package local. (Only set by Device).
    void setDevice(Device device) {
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RequestHeader{" + "name='" + name + "', value='" + value + "'}";
    }
}
