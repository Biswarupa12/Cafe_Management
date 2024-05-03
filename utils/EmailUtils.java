package com.cafe.cafe_management.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender eMailSender;

    public void sendSimpleMessage(String to, String subject, String message, List<String> list){
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("signature@gmail.com");
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(subject);
            if(list!=null && list.size() > 0)
                mailMessage.setCc(getCcArray(list));
            eMailSender.send(mailMessage);
            
    } 

    private String[] getCcArray(List<String> ccList) {
        String[] cc = new String[ccList.size()];
        for(int i=0;i<ccList.size();i++){
            cc[i] = ccList.get(i);
        }
        return cc;
}

public void forgetMail(String to,String subject,String password) throws MessagingException{
    MimeMessage message = eMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom("signature@gmail.com");
    helper.setTo(to);
    helper.setSubject(subject);
    String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
    message.setContent(htmlMsg,"text/html");
    eMailSender.send(message);
}
}