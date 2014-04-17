package com.pir.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pir.util.AppClock;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "msgGroup")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group extends AbstractPrimaryObject {

    private static final long serialVersionUID = 1L;


    public enum Subscription {
        FREE,
        PAID
    }

    @JsonProperty
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 15)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription", length = 35, nullable = false)
    private Subscription subscription = Subscription.FREE;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime subscriptionExpiresOn;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "group")
    private Set<UserGroupLink> userLinks = new HashSet<UserGroupLink>();


    public Group() {
    }

    public Group(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        if (subscriptionExpiresOn == null) {
            subscriptionExpiresOn = AppClock.now().plusYears(1);
        } else {
            subscriptionExpiresOn = subscriptionExpiresOn.plusYears(1);
        }
    }

    public DateTime getSubscriptionExpiresOn() {
        return subscriptionExpiresOn;
    }

    public void setSubscriptionExpiresOn(DateTime subscriptionExpiresOn) {
        this.subscriptionExpiresOn = subscriptionExpiresOn;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        for (UserGroupLink link : userLinks) {
            users.add(link.getUser());
        }
        return users;
    }

    public List<User> getAdminUsers() {
        List<User> admins = new ArrayList<User>();
        for (UserGroupLink link : userLinks) {
            if (link.getRole().equals(UserGroupLink.Role.ADMIN)) {
                admins.add(link.getUser());
            }
        }
        return admins;
    }


    public void removeUserLink(UserGroupLink link) {
        userLinks.remove(link);
    }


    public Set<UserGroupLink> getUserLinks() {
        return userLinks;
    }

    public List<UserGroupLink> getUserLinksList() {
        return new ArrayList<UserGroupLink>(userLinks);
    }
}
