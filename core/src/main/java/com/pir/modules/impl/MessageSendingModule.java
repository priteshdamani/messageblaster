package com.pir.modules.impl;

import com.pir.modules.IMessageSendingModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by pritesh on 12/14/13.
 */

@Component("messageSendingModule")
public class MessageSendingModule implements IMessageSendingModule{

    @Autowired
    private JavaMailSenderImpl mailSender;


    @Override
    public void sendMail(String from, String to, String subject, String msg, long messageRecordId, String fromName) throws MessagingException {

        if (to.contains("example.com")) {
            //hack, so that we dont really try to send this.
            return;
        }

        //msg = msg + "<img src=\"http://www.fantasybuzzer.com/pixel/" + messageRecordId + "\" />";

        MimeMessage message = mailSender.createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        try {
            InternetAddress address = new InternetAddress((from != null ? from : "no-reply@groupnotifier.com"), "Group Notifier");
            address.setPersonal(fromName);
            helper.setFrom(address);
        } catch (UnsupportedEncodingException e) {
            helper.setFrom(from != null ? from : "no-reply@groupnotifier.com");
        }
        helper.setSubject(subject);
        helper.setText(msg, true);

        mailSender.send(message);
    }

    @Override
    public void sendSmsMail(String from, String to, String msg) throws MessagingException {

        if (to.contains("example.com")) {
            //hack, so that we dont really try to send this.
            return;
        }

        int chunkSize = 137;

        int arraySize = ((int) Math.ceil((double) msg.length() / chunkSize));

        // split into multiple sms-emails
        String[] smsEmails = new String[arraySize];

        int index = 0;
        for (int i = 0; i < msg.length(); i = i + chunkSize) {
            if (msg.length() - i < chunkSize) {
                smsEmails[index++] = msg.substring(i);
            } else {
                smsEmails[index++] = msg.substring(i, i + chunkSize);
            }
        }

        for (String m : smsEmails) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from != null ? from : "no-reply@groupnotifier.com");
            message.setTo(to);
            message.setText(m);
            mailSender.send(message);
        }
    }
}
