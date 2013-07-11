/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.ForeignKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name="cookie")
public class ZombieCookie implements Serializable, Comparable<ZombieCookie> {

    private static final Logger log = LoggerFactory.getLogger(ZombieCookie.class);

    @Id
    @Size(max=36)
    @Column(length=36)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private Date inception;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSeen;

    @ManyToOne(optional=true, cascade = CascadeType.ALL)
    @ForeignKey(name="fk_cookie_parent")
    @JoinColumn(name="parent_id")
    private ZombieCookie parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ZombieCookie> children = new HashSet<ZombieCookie>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_cookie",
            joinColumns = @JoinColumn(name = "cookie_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ForeignKey(name = "fk_uc_cookie_id", inverseName = "fk_uc_user_id")
    private Set<OpenIdUser> users = new HashSet<OpenIdUser>();

    public ZombieCookie(Date inception) {
        this.id = UUID.randomUUID().toString();
        this.inception = inception;
    }

    public ZombieCookie() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInception() {
        return inception;
    }

    public void setInception(Date inception) {
        this.inception = inception;
    }

    public boolean isOlder(ZombieCookie otherCookie) {
        return getInception().before(otherCookie.getInception());
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public synchronized ZombieCookie getParent() {
        return parent;
    }

    public synchronized boolean hasParent() {
        return parent != null;
    }

    /*
     *  While getParent is public, setParent is private with extra checking to
     *  make sure we never create a loop.
     */
    private synchronized void setParent(ZombieCookie parent) {
        if (parent == null) {
            // we never unbind cookies, so this codepath should never happen
            log.error("attempt to set null parent");
            return;
        }

        if (parent.parent != null) {
            log.error("race condition detected, grandparent exists: cookie={} parent={}", id, parent.id);
            return;
        }

        if (id.equals(parent.id)) {
            log.error("attempt to set cookie={} as its own parent", id);
            return;
        }

        if (isOlder(parent)) {
            log.error("child={} is older than attempted parent={}", id, parent.id);
            return;
        }

        // flatten the hierarchy
        ZombieCookie[] unlinkedChildren = children.toArray(new ZombieCookie[children.size()]);
        children.clear();
        Arrays.sort(unlinkedChildren); // minimize chance of deadlock by accessing in order
        for (ZombieCookie child: unlinkedChildren) {
            child.setParent(parent);
        }

        this.parent = parent;
        parent.children.add(this);
    }

    // get/set children intentionally omitted

    public synchronized boolean isLinkedTo(ZombieCookie other) {
        return this.oldestRelative().getId().equals(other.oldestRelative().getId());
    }

    public synchronized ZombieCookie linkTo(ZombieCookie other) {
        ZombieCookie elder1 = oldestRelative();
        ZombieCookie elder2 = other.oldestRelative();

        if (elder1.id.equals(elder2.id)) {
            log.error("Cookies are already linked: {}", getId());
            return elder1;
        }

        // join the users before joining the trees
        elder1.linkToRecurse(elder2.users);
        elder2.linkToRecurse(elder1.users);

        ZombieCookie oldest;

       if (elder2.isOlder(elder1)) {
            elder1.setParent(elder2);
            oldest = elder2;
        } else {
            elder2.setParent(elder1);
            oldest = elder1;
        }

        return oldest;
    }


    public synchronized ZombieCookie oldestRelative() {
        ZombieCookie node = this;
        while (node.hasParent()) {
            node = node.getParent();
        }
        return node;
    }

    public synchronized void linkTo(OpenIdUser newUser) {
        oldestRelative().linkToRecurse(newUser, 1);
    }

    private synchronized void linkToRecurse(OpenIdUser user, int level) {

        if (level > 5) {
            // We flatten the hierarchy, so it would be abnormal to even go past 2.
            log.error("Aborting. Possible loop detected in cookie hierarchy user={} cookie={}",
                    user.getId(), getId());
            return;
        }

        if (users.add(user)) {
            user.getCookies().add(this);
        }
        // In theory we can stop if the parent cookie is linked to the
        // user, but I want to ensure data integrity so we check the
        // children too.
        for (ZombieCookie child : children) {
            child.linkToRecurse(user, level+1);
        }

    }

    private synchronized void linkToRecurse(Set<OpenIdUser> otherUsers) {
        for (OpenIdUser user: otherUsers) {
            linkToRecurse(user, 1);
        }
    }

    // not yet used
    public synchronized boolean isLinkedTo(OpenIdUser user) {
        return users.contains(user);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final ZombieCookie other = (ZombieCookie) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ZombieCookie other) {
        return id.compareTo(other.id);
    }
}
