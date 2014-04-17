package com.pir.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends AbstractPrimaryObject {

    private static final long serialVersionUID = 1L;

    public static List<String> CellCarrierTypesGet() {
        CellCarrier[] states = CellCarrier.values();
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < states.length; i++) {
            names.add(states[i].name());
        }

        return names;
    }


    public String smsEmailGet() {
        switch (getCellCarrier()) {
            case TMOBILE:
                return getMobileNumber() + "@tmomail.net";
            case ATT:
                return getMobileNumber() + "@txt.att.net";
            case CINGULAR:
                return getMobileNumber() + "@cingularme.com";
            case VERIZON:
                return getMobileNumber() + "@vtext.com";
            case VIRGIN_MOBILE:
                return getMobileNumber() + "@vmobl.com";
            case SPRINT:
                return getMobileNumber() + "@messaging.sprintpcs.com";
            case NEXTEL:
                return getMobileNumber() + "@messaging.nextel.com";
            case METRO_PCS:
                return getMobileNumber() + "@MyMetroPcs.com";
            case ALLTEL:
                return getMobileNumber() + "@message.alltel.com";
            case POWERTEL:
                return getMobileNumber() + "@ptel.com";
            case SUNCOM:
                return getMobileNumber() + "@tms.suncom.com";
            case US_CELLULAR:
                return getMobileNumber() + "@email.uscc.net";
            default:
                return null;
        }
    }


    public enum CellCarrier {
        TMOBILE,
        VERIZON,
        ATT,
        SPRINT,
        NEXTEL,
        METRO_PCS,
        ALLTEL,
        POWERTEL,
        SUNCOM,
        US_CELLULAR,
        CINGULAR,
        VIRGIN_MOBILE
    }

    @Column
    private boolean verified = false;

    @JsonProperty
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @JsonProperty
    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty
    @Column(length = 50)
    private String firstName;

    @JsonProperty
    @Column(length = 50)
    private String lastName;

    @JsonProperty
    @Column(length = 50)
    private String mobileNumber;

    @JsonProperty
    private String verificationRecord;

    @Enumerated(EnumType.STRING)
    @Column
    private CellCarrier cellCarrier;


    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "user")
    private Set<UserGroupLink> groupLinks = new HashSet<UserGroupLink>();

    public User() {
    }

    public User(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getVerificationRecord() {
        return verificationRecord;
    }

    public void setVerificationRecord(String verificationRecord) {
        this.verificationRecord = verificationRecord;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public CellCarrier getCellCarrier() {
        return cellCarrier;
    }

    public void setCellCarrier(CellCarrier cellCarrier) {
        this.cellCarrier = cellCarrier;
    }

    public void addGroup(Group group, UserGroupLink.Role role) {
        UserGroupLink gLink = linkGet(group.getId());
        if (gLink == null) {
            UserGroupLink link = new UserGroupLink(this, group, role);
            this.groupLinks.add(link);
        }
    }

    private UserGroupLink linkGet(Long groupId) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                return link;
            }
        }
        return null;
    }

    public void removeGroup(Group group) {
        UserGroupLink groupToRemove = linkGet(group.getId());
        if (groupToRemove != null) {
            group.removeUserLink(groupToRemove);
            this.removeGroupLink(groupToRemove);

            groupToRemove.setUser(null);
            groupToRemove.setGroup(null);
        }
    }

    private void removeGroupLink(UserGroupLink link) {
        groupLinks.remove(link);
    }

    public boolean isHigherRole(Long groupId) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                return (link.getRole().equals(UserGroupLink.Role.ADMIN) || (link.getRole().equals(UserGroupLink.Role.CAN_SEND_MESSAGES)));
            }
        }
        return false;
    }

    public boolean isAdminRole(Long groupId) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                return (link.getRole().equals(UserGroupLink.Role.ADMIN));
            }
        }
        return false;
    }

    public boolean changePrivilegeLevel(Long groupId, UserGroupLink.Role role) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                link.setRole(role);
                return true;
            }
        }
        return false;

    }

    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<Group>();
        for (UserGroupLink link : groupLinks) {
            groups.add(link.getGroup());
        }
        return groups;
    }

    public String getName() {
        StringBuilder name = new StringBuilder();
        if (StringUtils.isNotBlank(firstName)) {
            name.append(firstName);
            name.append(" ");
        }
        if (StringUtils.isNotBlank(lastName)) {
            name.append(lastName);
        }
        if (!StringUtils.isNotBlank(firstName) && !StringUtils.isNotBlank(lastName)) {
            name.append(username);
        }
        return name.toString();
    }

    public boolean changeNotificationOptions(Long groupId, boolean receiveEmails, boolean receiveTxt) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                link.setReceiveEmail(receiveEmails);
                link.setReceiveTxt(receiveTxt);
                return true;
            }
        }
        return false;
    }

    public boolean isReceivingEmail(Long groupId) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                return link.isReceiveEmail();
            }
        }
        return false;
    }

    public boolean isReceivingTxt(Long groupId) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                return link.isReceiveTxt();
            }
        }
        return false;
    }

    public UserGroupLink.Role getRole(Long groupId) {
        for (UserGroupLink link : groupLinks) {
            if (link.getGroup().getId().equals(groupId)) {
                return link.getRole();
            }
        }
        return null;
    }
}
