package com.pir.modules;

import com.pir.domain.Group;
import com.pir.domain.MessageRecord;
import com.pir.domain.User;
import com.pir.exceptions.TemplatingException;

import java.util.Map;

/**
 * Created by pritesh on 12/14/13.
 */
public interface IMessagingProcessingModule {

    public static String EMAIL_MSG = "emailMsg";
    public static String EMAIL_VERIFY_USER_SIGNUP = "userVerify";
    public static String EMAIL_GROUP_CREATED = "groupCreated";
    public static String EMAIL_RESET_PASSWORD = "resetPassword";

    public static String CAMPAIGN_MESSAGING = "M";
    public static String CAMPAIGN_VERIFY_USER_SIGNUP = "V";
    public static String CAMPAIGN_GROUP_CREATED = "G";
    public static String CAMPAIGN_EMAIL_RESET_PASSWORD = "R";

    String processSubjectTemplate(String templateName, Map vars) throws TemplatingException;

    String processTemplate(String templateName, Map vars) throws TemplatingException;

    void sendMessageRecords(int pageSize);

    MessageRecord sendVerificationEmail(User user) throws TemplatingException;

    MessageRecord sendPasswordResetEmail(User user) throws TemplatingException;

    long totalMessageRecordsGet();

    MessageRecord sendGroupCreatedEmail(Group group, User user) throws TemplatingException;
}
