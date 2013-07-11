/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Index;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="oid_user")
public class OpenIdUser implements UserDetails  {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @NotNull
    @Size(min = 1, max=255)
    @Column(length=255, unique = true)
    private String openId;

    private Timestamp firstLogin;
    private Timestamp lastLogin;

    @Size(max=254)
    @Column(length=254)
    @Index(name="email_index")
    private String email;

    @Size(max=120)
    @Column(length=120)
    private String fullName;

    @Size(max=60)
    @Column(length=60)
    private String firstName;

    @Size(max=60)
    @Column(length=60)
    private String lastName;

    @Size(max=30)
    @Column(length=30)
    private String roleName;

    /*
     * The request host is of interest because Google is using different
     * id URLs for different request hosts.
     */
    @Size(max=255)
    @Column(length=255, name="rqst_host")
    private String requestHost;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<ZombieCookie> cookies = new HashSet<ZombieCookie>();

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

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Timestamp getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Timestamp firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRoleName());
    }

    @Override
    public String getPassword() {
        return null;  // OpenId login, we don't have passwords
    }

    @Override
    public String getUsername() {
        return getOpenId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(roleName);
    }

    public boolean isAnonymous() {
        return email == null || email.isEmpty();
    }

    public String getRequestHost() {
        return requestHost;
    }

    public void setRequestHost(String requestHost) {
        this.requestHost = requestHost;
    }


    // Package scope intentional.  Only needed by ZomebieCookie.
    Set<ZombieCookie> getCookies() {
        return cookies;
    }

    // set cookies intentionally omitted


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OpenIdUser other = (OpenIdUser) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "OpenIdUser{"
                + "id=" + id
                + ", version=" + version
                + ", openId=" + openId
                + ", email=" + email
                + ", fullName=" + fullName
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", roleName=" + roleName
                + '}';
    }

}
