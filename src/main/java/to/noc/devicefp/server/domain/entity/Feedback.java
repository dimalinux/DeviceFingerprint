/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Feedback implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;

    @ManyToOne
    @JoinColumn(name="user_id")
    @ForeignKey(name="fk_feedback_to_user")
    @OnDelete(action = OnDeleteAction.CASCADE)    
    private OpenIdUser user;
    
    @Size(max=10240)
    @Column(length=10240)
    private String message;
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public OpenIdUser getUser() {
        return user;
    }

    public void setUser(OpenIdUser user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "Feedback{" + "id=" + id + ", version=" + version + ", user=" + user + ", message=" + message + '}';
    }
}
