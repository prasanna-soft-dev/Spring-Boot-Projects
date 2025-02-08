package com.sprintify.task.Service;

import com.sprintify.task.Entity.EmailEntity;

public interface EmailService {
    void sendSimpleEmail(String recipient,String msgBody,String subject);
    String sendMailWithAttachment(EmailEntity email);


}
