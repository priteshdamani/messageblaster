package com.pir.services;

import com.pir.domain.Group;
import com.pir.domain.Message;
import com.pir.domain.User;
import com.pir.domain.UserGroupLink;
import com.pir.dto.ChargeAttemptResultDto;
import com.pir.dto.ProfileDto;
import com.pir.dto.UserPublicDto;
import com.pir.exceptions.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/8/13
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IBusinessService {

    User registerUser(User user) throws UsernameAlreadyUsedException, EmailAlreadyUsedException;

    boolean resendVerificationEmail(Long userId);

    User registerUserAndGroup(User user, String groupName, String groupDescription) throws EmailAlreadyUsedException, UsernameAlreadyUsedException;

    Group registerGroup(String groupName, String groupDescription, Long userId);

    User userGet(long userId);

    Long authenticate(String identifier, String password);

    Group groupGet(Long groupId);

    Group searchGroup(String groupCode);

    Group joinGroup(String groupCode, Long userId);

    User registerUserAndJoinGroup(User user, String groupCode) throws EmailAlreadyUsedException, UsernameAlreadyUsedException;

    List<UserPublicDto> groupMembersGet(Long groupId);

    Message sendMessage(Long sentBy, Long sentTo, String subject, String body);

    List<Message> messagesGet(Long groupId, int pageSize);

    Group groupUpdate(Long groupId, String groupName, String groupDescription);

    ChargeAttemptResultDto pay(Long paidBy, Long groupId, String ccName, String ccNum, String ccExpMon, String ccExpYear, String ccCvc);

    Group deleteMembership(Long deletionRequestBy, Long userId, Long groupId) throws NotAnAdminException;

    void privilegeChange(Long requestedBy, Long userId, Long groupId, UserGroupLink.Role privilegeLevel) throws NotAnAdminException;

    void updateProfile(ProfileDto info) throws EmailAlreadyUsedException;

    void changePassword(Long userId, String password);

    void notificationOptionsUpdate(Long userId, Long groupId, boolean receiveEmails, boolean receiveTxt);

    void verifyEmail(long userId, String code) throws InvalidVerifyEmailCodeException;

    boolean sendPasswordReset(String username, String email) throws EmailNotFoundException, UsernameNotFoundException;

    long totalMessageRecordsGet();
}
