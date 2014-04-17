package com.pir.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by pritesh on 12/10/13.
 */

@Entity
@Table(name="message")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Message extends AbstractPrimaryObject {


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User createdBy;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "group_id")
    private Group createdFor;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = true)
    private String body;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime processedDate;

    public Message() {
    }

    public Message(User createdBy, Group createdFor, String subject) {
        this.createdBy = createdBy;
        this.createdFor = createdFor;
        this.subject = subject;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Group getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(Group createdFor) {
        this.createdFor = createdFor;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public DateTime getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(DateTime processedDate) {
        this.processedDate = processedDate;
    }
}
