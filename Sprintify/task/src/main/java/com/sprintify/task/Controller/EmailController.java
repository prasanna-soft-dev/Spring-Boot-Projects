package com.sprintify.task.Controller;

import com.sprintify.task.Entity.EmailEntity;
import com.sprintify.task.Entity.UserEntity;
import com.sprintify.task.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private EmailService emailService;



    @PostMapping("/sendAttachment")
    public String
        sendAttachment(@RequestBody EmailEntity email){
        return emailService.sendMailWithAttachment(email);
    }


}
