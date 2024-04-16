package com.nilknow.yifanerp2.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@Slf4j
public class MailService {
    public void send(String toEmails, String subject, String body) {
        // 邮件发送者的用户名和密码
        final String username = "494939649@qq.com";
        final String password = "klzualuffrqjbgje";

        // 邮件相关信息
        String fromEmail = "494939649@qq.com";

        // 邮件服务器配置
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "587");

        // 创建会话对象，用于与邮件服务器进行通信
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 创建邮件消息对象
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails));
            message.setSubject(subject);
            message.setText(body);

            // 发送邮件
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Email sent failed", e);
        }
    }
}
