/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*
 * This table is used directly by spring libraries, so the table and column
 * names should not be changed.
 */
@Entity
@Table(name="persistent_logins")
public class PersistentLogins {
   
    @ManyToOne(optional=false)
    @JoinColumn(name="username", referencedColumnName="openId")
    @ForeignKey(name="fk_logins_to_user")
    @OnDelete(action = OnDeleteAction.CASCADE)    
    private OpenIdUser oidUser;
        
    @Id
    @Column(length=64)
    private String series;
    
    @Column(length=64, nullable=false)
    private String token;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false, name="last_used")
    private Date lastUsed;

    public OpenIdUser getOidUser() {
        return oidUser;
    }

    public void setOidUser(OpenIdUser oidUser) {
        this.oidUser = oidUser;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

}
