package org.Stasy.PublicPrivacyAppBackendHeroku.ResetPassword;

import jakarta.servlet.http.HttpServletResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.ResetPassword.requestPOJO.EmailRequest;
import org.Stasy.PublicPrivacyAppBackendHeroku.ResetPassword.requestPOJO.EmailService;
import org.Stasy.PublicPrivacyAppBackendHeroku.User.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.User.UserRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.User.UserServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/collaborator")
public class PasswordController {


    @Value("${app.base-url}")
    private String baseUrl;

    private final UserServiceInterface userServiceInterface;

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder bCryptPasswordEncoder;

    public PasswordController(UserRepository userRepository, EmailService emailService, PasswordEncoder bCryptPasswordEncoder,UserServiceInterface userServiceInterface) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userServiceInterface=userServiceInterface;
    }


    @GetMapping("/resetPassword1")
    public ResponseEntity<String> displayForgotPasswordPage(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");
        return ResponseEntity.status(HttpStatus.OK).build();
    }


//

    @PostMapping("/resetPassword2")
    public ResponseEntity<String> requestPasswordReset(@RequestBody EmailRequest emailRequest, HttpServletResponse response) {
        String email = emailRequest.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account doesn't exist.");
        } else {
            user.setResetToken(UUID.randomUUID().toString());
            // Save token to database
            userRepository.save(user);
            userServiceInterface.save(user);


            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + baseUrl
                    + "resetPassword3?token=" + user.getResetToken());

            System.out.println("token: "+user.getResetToken());
            emailService.sendEmail(passwordResetEmail);
            userRepository.save(user);

            response.setHeader("Access-Control-Allow-Origin", "http://public-privacy.xyz");
            return ResponseEntity.ok("A password reset link has been sent to " + email);
        }
    }


//    @GetMapping("/resetPassword3")
//    public ResponseEntity<?> displayResetPasswordPage(@RequestParam("token") String token, HttpServletResponse response) {
//        User user = userRepository.findUserByResetToken(token);
//
//        if (!Objects.equals(user,null)) {
//            response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");
//
//            if (!Objects.equals(user, null)) {
//                response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");
//
//                String resetUrl = "https://public-privacy.xyz/#/collaborator/enter-new-password";
//
//                String message = "Please click the link below to reset your password:<br>"
//                        + "<a href='" + resetUrl + "'>" + resetUrl + "</a>, and the Token is "+token;
//
//                System.out.println("token at step 3: " + token);
//
//                return ResponseEntity.ok(message);
//            } else {
//                response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password reset link. Please try again later");
//            }
//        } return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }

    @GetMapping("/resetPassword3")
    public ResponseEntity<?> displayResetPasswordPage(@RequestParam("token") String token, HttpServletResponse response) {
        User user = userRepository.findUserByResetToken(token);

        if (!Objects.equals(user, null)) {
            response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");

            String resetUrl = "https://public-privacy.xyz/#/collaborator/enter-new-password";

            String message = "<div style='font-size: 16px;'>Please click the link below to reset your password:<br>"
                    + "<a href='" + resetUrl + "'>" + resetUrl + "</a>, and the Token is <span style='font-size: 18px; cursor: pointer; user-select: all;' onclick='copyToClipboard(this)'>" + token + "</span></div>";

            System.out.println("token at step 3: " + token);

            return ResponseEntity.ok(message);
        } else {
            response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password reset link. Please try again later");
        }
    }




    // Process reset password form
    @PostMapping("/resetPassword4")
    public ResponseEntity<String> setNewPassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest, HttpServletResponse response ) {
        System.out.println("Received request: " + passwordUpdateRequest);

        String token = passwordUpdateRequest.getToken();
        String newPassword = passwordUpdateRequest.getPassword();

        User user = userRepository.findUserByResetToken(token);

        if (!Objects.equals(user,null)) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            user.setResetToken(null);
            userRepository.save(user);
            userServiceInterface.save(user);
            response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");
            return ResponseEntity.ok("Password successfully reset. You may now login.");
        } else {
            response.setHeader("Access-Control-Allow-Origin", "https://public-privacy.xyz");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password reset link.");
        }
    }

    // Exception handler
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing parameters.");
    }
}

