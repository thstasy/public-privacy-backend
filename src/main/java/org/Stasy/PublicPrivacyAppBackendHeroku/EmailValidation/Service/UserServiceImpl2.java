package org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service;

import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Entity.ConfirmationToken;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.ConfirmationTokenRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.UserRepository2;
import org.Stasy.PublicPrivacyAppBackendHeroku.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl2 implements UserService2 {

    @Autowired
    private UserRepository2 userRepository2;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;


    @Override
    public ResponseEntity<?> saveUser(User user) {

        if (userRepository2.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        userRepository2.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration");
        mailMessage.setText("To confirm your account, please click here: "
                + baseUrl + "/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository2.findByEmailIgnoreCase(token.getUserEntity().getEmail());
            user.setEnabled(true);
            userRepository2.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
}
