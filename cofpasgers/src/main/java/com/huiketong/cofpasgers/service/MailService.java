package com.huiketong.cofpasgers.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String fromuser;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JavaMailSender mailSender;

    public void sayHello() {
        System.out.println("HelloWorld");
    }

    /**
     * 发送简单文本邮件
     *
     * @param touser
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String touser, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(touser);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(fromuser);
        mailSender.send(message);
    }

    /**
     * 发送html形式的邮件
     *
     * @param touser
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String touser, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromuser);
        helper.setTo(touser);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    /**
     * 发送带附件的邮件,可以发送多个附件
     *
     * @param touser
     * @param subjet
     * @param content
     * @param filePath
     * @throws MessagingException
     */
    public void sendAttachmentsMail(String touser, String subjet, String content, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromuser);
        helper.setTo(touser);
        helper.setSubject(subjet);
        helper.setText(content, true);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);
        mailSender.send(message);
    }

    /**
     * 发送邮件内容带图片格式的邮件
     *
     * @param touser
     * @param subjet
     * @param content
     * @param rscPath
     * @param rscId
     */
    public void sendInlinResourceMail(String touser, String subjet, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        logger.info("发送邮件开始:{},{},{},{},{}", touser, subjet, content, rscPath, rscId);
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromuser);
            helper.setTo(touser);
            helper.setSubject(subjet);
            helper.setText(content, true);
            FileSystemResource resource = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, resource);
            mailSender.send(message);
            logger.info("发送静态邮件成功");
        } catch (MessagingException e) {
            logger.error("发送静态邮件失败:{}", e);
        }
    }
}