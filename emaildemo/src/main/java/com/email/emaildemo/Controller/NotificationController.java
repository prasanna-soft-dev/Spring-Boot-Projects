package com.email.emaildemo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.emaildemo.Entity.User;
import com.email.emaildemo.Service.NotificationService;

@RestController
public class NotificationController {
    
    private Logger logger = LoggerFactory.getLogger(NotificationController.class);


    @Autowired
    NotificationService notificationService;

    @RequestMapping("/register")
    public String register(){
        return "PLEASE REGISTER";
    }

    @RequestMapping("/success")
    public String successAccess(){
        User u = new User();

        u.setFirstName("prasanna");
        u.setLastName("Thiyagarajan");
        u.setEmail("rebel200318@gmail.com");
        try{
        notificationService.sendNotification(u);
    }
    catch(MailException e){
        logger.info(e.getMessage());
    }

    return "Email Sent Successfully";
    }

    
}
