package org.Stasy.PublicPrivacyAppBackendHeroku.ResetPassword.requestPOJO;

import org.springframework.mail.SimpleMailMessage;


public interface EmailService {
    public void sendEmail(SimpleMailMessage email);
}