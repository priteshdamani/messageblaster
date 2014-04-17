package com.pir.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pritesh on 12/10/13.
 */


@Entity
@Table(name="userGroupLink")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserGroupLink extends AbstractChildObject{


    public static List<String> RoleTypesGet() {
        Role[] states = Role.values();
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < states.length; i++) {
            names.add(states[i].name());
        }

        return names;
    }

    public enum Role
    {
        ADMIN,
        CAN_SEND_MESSAGES,
        CAN_RECEIVE_MESSAGES
    }

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "groupRole", length = 35, nullable = false)
    private Role role;

    @Column
    private boolean receiveEmail = true;

    @Column
    private boolean receiveTxt = true;

    @Column
    private boolean receiveIphonePush = true;

    @Column
    private boolean receiveAndroidPush = true;


    public UserGroupLink() {
    }

    public UserGroupLink(User user, Group group, Role role) {
        this.user = user;
        this.group = group;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(boolean receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public boolean isReceiveTxt() {
        return receiveTxt;
    }

    public void setReceiveTxt(boolean receiveTxt) {
        this.receiveTxt = receiveTxt;
    }

    public boolean isReceiveIphonePush() {
        return receiveIphonePush;
    }

    public void setReceiveIphonePush(boolean receiveIphonePush) {
        this.receiveIphonePush = receiveIphonePush;
    }

    public boolean isReceiveAndroidPush() {
        return receiveAndroidPush;
    }

    public void setReceiveAndroidPush(boolean receiveAndroidPush) {
        this.receiveAndroidPush = receiveAndroidPush;
    }
}
