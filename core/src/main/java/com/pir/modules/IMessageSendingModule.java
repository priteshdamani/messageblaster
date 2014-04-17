package com.pir.modules;

import javax.mail.MessagingException;

/**
 * Created by pritesh on 12/14/13.
 */
public interface IMessageSendingModule {

    void sendMail(String from, String to, String subject, String msg, long messageRecordId, String name) throws MessagingException;

    void sendSmsMail(String fromEmail, String toEmail, String body) throws MessagingException;
}
