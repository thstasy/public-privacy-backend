package org.Stasy.PublicPrivacyAppBackendHeroku.Draft;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtDecoder;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtGenerator;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
import org.Stasy.PublicPrivacyAppBackendHeroku.User.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.User.UserServiceImpl;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/forum")
public class DraftController {

    private final JwtGenerator jwtGenerator;
    private final JwtDecoder jwtDecoder;
    private final UserServiceImpl userServiceImpl;
    private final DraftRepository draftRepository;
    private final CustomDraftRepositoryImpl customDraftRepositoryImpl;

    @Autowired
    //constructor
    public DraftController(JwtGenerator jwtGenerator, JwtDecoder jwtDecoder, UserServiceImpl userServiceImpl, DraftRepository draftRepository, CustomDraftRepositoryImpl customDraftRepositoryImpl) {
        this.jwtGenerator = jwtGenerator;
        this.jwtDecoder = jwtDecoder;
        this.userServiceImpl = userServiceImpl;
        this.draftRepository = draftRepository;
        this.customDraftRepositoryImpl = customDraftRepositoryImpl;
    }

    //read all drafts
    @GetMapping("/drafts")
    public ResponseEntity<?> readDrafts(@RequestHeader(name = "loginToken") String loginToken, HttpServletResponse response) {

        System.out.println("The loginToken " + loginToken);

        try {
            String email = jwtDecoder.decodeUserEmailFromLoginToken(loginToken);
            User user = userServiceImpl.findUserByEmail(email);

            if (user != null && email != null) {
                response.addHeader("Access-Control-Max-Age", "604800");
                List<Draft> drafts = draftRepository.findAllByOrderByUpdatedAtDesc();
                return new ResponseEntity<>(drafts, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The Collaborator doesn't exist in the Database. Please contact us.");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The Collaborator doesn't exist in the Database. Please contact us.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }


    //create a draft
    @PostMapping("/drafts")
    public ResponseEntity<?> saveDraft(@RequestHeader(name = "loginToken") String loginToken, @RequestBody Draft draft, HttpServletResponse response) {

        User user = null;

        System.out.println(draft.toString());
        try {
            String username = jwtDecoder.decodeUsernameFromLoginToken(loginToken);
            user = userServiceImpl.findUserByUsername(username);

            if (!Objects.equals(user, null)) {
                draft.setCollaboratorName(username);

                customDraftRepositoryImpl.saveDraftWithCollaboratorName(draft, username);

                return ResponseEntity.status(HttpStatus.CREATED).body("Draft saved successfully.");
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //read a draft by ID
    @GetMapping("/drafts/{id}") public ResponseEntity<?> readDraft(@PathVariable Long id, HttpServletResponse response) throws UserNotFoundException {
        Optional<Draft> draft = draftRepository.findById(id);
        System.out.println(draft);
        try {
            if (Objects.equals(draft, null)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Sorry, the content is deleted.");
            }
        } catch (NullPointerException e) {
            System.out.println("A NullPointerException occurred: " + e.getMessage());
        }
        try {
            if (!Objects.equals(id, null)) {
                return ResponseEntity.ok(draft);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Draft not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/drafts")
    public ResponseEntity<?> updateDraft(@CookieValue(name = "loginToken") String loginToken, @RequestBody Draft draft, HttpServletResponse response) throws UserNotFoundException {

        System.out.println(draft);
        String username = jwtDecoder.decodeUsernameFromLoginToken(loginToken);
        Draft existingDraft = draftRepository.findDraftById(draft.getId());

        if (userServiceImpl.findUserByUsername(username).isEnabled() && username.equals(existingDraft.getCollaboratorName())) {
            // Update the draft and save
            customDraftRepositoryImpl.saveDraftWithCollaboratorName(draft, username);

            // Set cookies and headers
            Cookie loginCookie = new Cookie("loginToken", loginToken);
            loginCookie.setHttpOnly(true);
            loginCookie.setMaxAge(604800);
            response.setContentType("application/json");
            response.addCookie(loginCookie);
            response.addHeader("Access-Control-Expose-Headers", "loginToken");
            response.addHeader("Access-Control-Max-Age", "604800");

            // Return updated drafts
//            List<Draft> updatedDrafts = draftRepository.findAllByCollaboratorNameOrderByUpdatedAtDesc(username);
            return ResponseEntity.ok("Opinion updated successfully!");
        } else {
            if (!userServiceImpl.findUserByUsername(username).isEnabled()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not enabled.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the initial author is able to modify the content.");
            }
        }
    }


    @Transactional
    @DeleteMapping("/drafts/{id}")
    public ResponseEntity<?> deleteDraft(@RequestHeader(name = "Logintoken") String loginToken, @PathVariable Long id, HttpServletResponse response) {

        String username = jwtDecoder.decodeUsernameFromLoginToken(loginToken);
        Draft draft = draftRepository.findDraftById(id);

        if (draft == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Draft not found.");
        }

        String authorUsername = draft.getCollaboratorName();
        if (!username.equals(authorUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this draft.");
        }

        draftRepository.deleteById(id);

        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
        response.addHeader("Access-Control-Expose-Headers", "loginToken");
        response.addHeader("Access-Control-Max-Age", "604800");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
