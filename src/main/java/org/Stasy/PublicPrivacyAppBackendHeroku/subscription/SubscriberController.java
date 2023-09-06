package org.Stasy.PublicPrivacyAppBackendHeroku.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private EmailRepository emailRepository;

    public SubscriberController(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @GetMapping("/subscriber-emails")
    public ResponseEntity<List<String>> getAllSubscriberEmails() {
        List<EmailEntity> subscribers = emailRepository.findAll();
        List<String> emailAddresses = subscribers.stream()
                .map(EmailEntity::getEmail)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emailAddresses);
    }
}

