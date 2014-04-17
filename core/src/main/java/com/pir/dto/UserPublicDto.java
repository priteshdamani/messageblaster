package com.pir.dto;

import com.pir.domain.UserGroupLink;

import java.io.Serializable;

/**
 * Created by pritesh on 12/10/13.
 */
public class UserPublicDto implements Serializable{
    private final Long groupId;
    private Long id;
    private String name;
    private String email;
    private UserGroupLink.Role role;
    private boolean emailEnabled;
    private boolean txtEnabled;

    public UserPublicDto(Long id, Long groupId, String name, String email, UserGroupLink.Role role, boolean emailEnabled, boolean txtEnabled) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.emailEnabled = emailEnabled;
        this.txtEnabled = txtEnabled;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserGroupLink.Role getRole() {
        return role;
    }

    public void setRole(UserGroupLink.Role role) {
        this.role = role;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isTxtEnabled() {
        return txtEnabled;
    }
}
