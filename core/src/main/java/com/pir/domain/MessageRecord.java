package com.pir.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by pritesh on 12/10/13.
 */

@Entity
@Table(name="messageRecord")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MessageRecord extends AbstractPrimaryObject {

    public enum MessageType {

        EMAIL(1),
        SMS(2),
        EMAIL_SMS(3);

        private int code;

        private MessageType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum MessagePriority {

        LOW(0),
        MEDIUM(1),
        HIGH(2),
        IMMEDIATE(2);

        private int priority;

        private MessagePriority(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "message_id", nullable = true)
    private Message createdThrough;

    @Column
    private MessageType type;

    @Column
    private MessagePriority priority;

    @Column(name = "campaign", nullable = true, length = 30)
    private String campaign;

    @Column(columnDefinition = "longtext")
    private String body;

    @Column(length = 200)
    private String subject;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime sentDate;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime errorDate;

    @Column(columnDefinition = "text")
    private String errorString;

    @Column(length=50, name = "toEmail")
    private String toEmail;

    @Column(length=50, name = "fromEmail")
    private String fromEmail;

    @Column(length=50, name = "fromName")
    private String fromName;


    public MessageRecord() {
    }

    public MessageRecord(MessageType type, MessagePriority priority, String campaign, String body, String subject, String toEmail, Message createdThrough, String fromEmail, String name) {
        this.type = type;
        this.priority = priority;
        this.campaign = campaign;
        this.body = body;
        this.subject = subject;
        this.toEmail = toEmail;
        this.createdThrough = createdThrough;
        this.fromEmail = fromEmail;
        this.fromName = name;
    }

    public MessageRecord(MessageType type, MessagePriority priority, String campaign, String body, String subject, String toEmail, Message createdThrough) {
        this(type,priority,campaign,body,subject,toEmail,createdThrough,null,null);
    }

    public Message getCreatedThrough() {
        return createdThrough;
    }

    public void setCreatedThrough(Message createdThrough) {
        this.createdThrough = createdThrough;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessagePriority getPriority() {
        return priority;
    }

    public void setPriority(MessagePriority priority) {
        this.priority = priority;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public DateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(DateTime sentDate) {
        this.sentDate = sentDate;
    }

    public DateTime getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(DateTime errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
}
