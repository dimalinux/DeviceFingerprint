/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="plugins")
public class Plugin implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
        
    @ManyToOne(optional=false)
    @ForeignKey(name="fk_plugin_to_dev")
    @JoinColumn(name="device_id")    
    protected Device device;
    
    @Size(max=100)
    @Column(length=100)
    private String name;
    
    @Size(max=50)
    @Column(length=50)
    private String version;

    @Size(max=300)
    @Column(length=300)
    private String description;
    
    @Size(max=120)
    @Column(length=120)
    private String filename;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
