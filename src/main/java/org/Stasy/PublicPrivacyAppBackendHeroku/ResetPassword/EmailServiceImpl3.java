package org.Stasy.PublicPrivacyAppBackendHeroku.ResetPassword;

import org.Stasy.PublicPrivacyAppBackendHeroku.ResetPassword.requestPOJO.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailServiceForResetPwd")
public class EmailServiceImpl3 implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }
}