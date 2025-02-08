package com.sprintify.task.Service.Implementation;

import com.sprintify.task.Entity.EmailEntity;
import com.sprintify.task.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.File;

@EnableAsync
@Service
public class EmailImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    public void sendSimpleEmail(String recipient,String msgBody,String subject){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(recipient);
            simpleMailMessage.setText(msgBody);
            simpleMailMessage.setSubject(subject);

            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            e.printStackTrace(); // Print actual error message
        }
    }

    public String sendMailWithAttachment(EmailEntity email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(email.getRecipient());
            mimeMessageHelper.setText(email.getMsgBody());
            mimeMessageHelper.setSubject(email.getSubject());


            FileSystemResource fileSystemResource = new FileSystemResource(new File(email.getAttachment()));

            mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);


            javaMailSender.send(mimeMessage);

            return "Mail successfully sent with attachment";


        } catch (MessagingException e) {
            return "Error while sending mail with Attachment!!";
        }
    }

}
