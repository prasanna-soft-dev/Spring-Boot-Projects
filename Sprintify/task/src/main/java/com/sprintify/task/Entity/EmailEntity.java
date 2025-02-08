package com.sprintify.task.Entity;

import jakarta.persistence.Entity;


public class EmailEntity {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public EmailEntity() {
    }

    public EmailEntity(String recipient, String msgBody, String subject, String attachment) {
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
        this.attachment = attachment;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}

