package org.Stasy.PublicPrivacyAppBackendHeroku.subscription;

import jakarta.servlet.http.HttpServletResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

    @Value("${app.Domain}")
    private String homepage;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailService emailService;

//    @Autowired
//    private SubscriptionConfirmationTokenRepository subscriptionConfirmationTokenRepository;

    public EmailController(EmailRepository emailRepository, EmailService emailService) {
        this.emailRepository = emailRepository;
        this.emailService = emailService;
//        this.subscriptionConfirmationTokenRepository = subscriptionConfirmationTokenRepository;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody EmailEntity emailEntity, HttpServletResponse response) {

        if (emailEntity == null) {
            return new ResponseEntity<>("Invalid Email", HttpStatus.CONFLICT); // 409
        }

        if (emailRepository.existsByEmail(emailEntity.getEmail())) {
            return new ResponseEntity<>("Email Already Subscribed", HttpStatus.CONFLICT); // 409
        }

        try {
            // Send verification token
            // SubscriptionConfirmationToken subscriptionConfirmationToken = new SubscriptionConfirmationToken(emailEntity);
            // subscriptionConfirmationTokenRepository.save(subscriptionConfirmationToken);
            //make sure it really saves  the confirmation data here.

            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            emailEntity.setEnabled(true);
            emailRepository.save(emailEntity);

            mailMessage.setTo(emailEntity.getEmail());
            mailMessage.setSubject("Subscription Confirmation");

            mailMessage.setText("This is to confirm that your email has been registered on subscription list! :)");

//          mailMessage.setText("To confirm your subscription, please click here: "
//          + homepage + "confirm-subscription?token=" + subscriptionConfirmationToken.getConfirmationToken());
            emailService.sendEmail(mailMessage);

            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201

        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception properly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }
//    @RequestMapping(value = "/confirm-subscription", method = { RequestMethod.GET, RequestMethod.POST })
//    public ResponseEntity<?> confirmSubscription(@RequestParam("token")String confirmationToken) {
//        // This is the part of confirming the email
//        SubscriptionConfirmationToken token;
//        token = subscriptionConfirmationTokenRepository.findByConfirmationToken(confirmationToken);
//
//        if(confirmationToken != null)
//        {
//            EmailEntity emailEntity = emailRepository.findByEmailIgnoreCase(token.getEmailEntity().getEmail());
//            emailEntity.setEnabled(true);
//            emailRepository.save(emailEntity);
//            String message = "Email verified successfully!";
//
//            return ResponseEntity.ok(message);
//        }
//        return ResponseEntity.badRequest().body("Error: Couldn't verify email. Please try again with the address.");
//    }
}
