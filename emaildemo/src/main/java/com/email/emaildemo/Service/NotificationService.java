package com.email.emaildemo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.email.emaildemo.Entity.User;

@Service
public class NotificationService {
    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(User u)throws MailException{

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(u.getEmail());
        simpleMailMessage.setFrom("tgdp2418@gmail.com");
        simpleMailMessage.setText("Finally did some usefull project");
        simpleMailMessage.setSubject("Sent using spring boot");
        javaMailSender.send(simpleMailMessage);
    }
}
