package org.Stasy.PublicPrivacyAppBackendHeroku.Opinion;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtDecoder;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtGenerator;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository.CustomOpinionRepositoryImpl;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository.OpinionsRepository;
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

@RestController
@RequestMapping("/forum")
public class OpinionsController {

    private final JwtGenerator jwtGenerator;
    private final JwtDecoder jwtDecoder;
    private final UserServiceImpl userServiceImpl;
    private final OpinionsRepository opinionsRepository;
    private final CustomOpinionRepositoryImpl customOpinionRepositoryImpl;

    @Autowired
    //constructor
    public OpinionsController(JwtGenerator jwtGenerator, JwtDecoder jwtDecoder, UserServiceImpl userServiceImpl, OpinionsRepository opinionsRepository,CustomOpinionRepositoryImpl customOpinionRepositoryImpl) {
        this.jwtGenerator = jwtGenerator;
        this.jwtDecoder = jwtDecoder;
        this.userServiceImpl = userServiceImpl;
        this.opinionsRepository = opinionsRepository;
        this.customOpinionRepositoryImpl = customOpinionRepositoryImpl;
    }

    //simply testing
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //view all opinions
    @GetMapping("/opinions")
    public ResponseEntity<?> readOpinions(@RequestHeader(name = "loginToken") String loginToken,HttpServletResponse response) throws UserNotFoundException {

        System.out.println("The loginToken From GetOpinions"+loginToken);
        //assume the opposite
        boolean is200=false;
        boolean is403=true;
        try {
            String email = jwtDecoder.decodeUserEmailFromLoginToken(loginToken);
            User user = userServiceImpl.findUserByEmail(email);

            if(!Objects.equals(user,null) && !Objects.equals(email,null)){
                is200=true;
                is403=false;
            }
        } catch (UserNotFoundException e) {
            is403=true;
            e.printStackTrace();
        } catch (Exception e) {
            is200=false;
            e.printStackTrace();
        }finally {
            if(is200){
                //原本的Token沒有修改，塞進新的Cookie裡
                response.addHeader("Access-Control-Max-Age", "3600");
                return new ResponseEntity<List<Opinion>>(opinionsRepository.findAllByOrderByUpdatedAtDesc(), HttpStatus.OK);//no worries about token, because data is sent directly back in the response.
            }else if(is403){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The Collaborator doesn't exist in the Database. Please contact us.");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
            }
        }
    }


    @GetMapping("/opinions/{id}")
    public ResponseEntity<?> readOpinion(@PathVariable Long id, HttpServletResponse response) throws UserNotFoundException {

        Opinion opinion = opinionsRepository.findOpinionById(id);

        try {
            if (Objects.equals(opinion, null)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Sorry, the content is deleted.");
            }
        } catch (NullPointerException e) {
            System.out.println("A NullPointerException occurred: " + e.getMessage());
        }

        try {
            if (!Objects.equals(id, null)) {
                return ResponseEntity.ok(opinion); // Return the opinion object in the response body
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Opinion not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    //C==> create an opinion
    @PostMapping("/opinions")
    public ResponseEntity<?> createOpinions(@RequestHeader(name = "loginToken") String loginToken,@RequestBody Opinion opinion, HttpServletResponse response) {

        User user=null;

        try {
            String username = jwtDecoder.decodeUsernameFromLoginToken(loginToken);
            user=userServiceImpl.findUserByUsername(username);//Oh I see, opinion doens't have collaboratorName yet!
            System.out.println(user);

            if(!Objects.equals(user,null)){
                opinion.setCollaboratorName(username);
                customOpinionRepositoryImpl.saveOpinionWithCollaboratorName(opinion,username);
                System.out.println("Opinion"+opinion);

            }
        }
        catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        finally {

            Cookie loginCookie = new Cookie("loginToken", loginToken);
            loginCookie.setHttpOnly(true);
            loginCookie.setMaxAge(604800);
            response.setContentType("application/json");
            response.addCookie(loginCookie);

            response.addHeader("Access-Control-Expose-Headers", "loginToken");
            response.addHeader("Access-Control-Max-Age", "604800");

            System.out.println("I entered 201 in post, printed before 'ResponseEntity'");

            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        }

    }

    @PutMapping("/opinions")
    public ResponseEntity<?> updateOpinion(@RequestHeader(name = "loginToken") String loginToken,@RequestBody Opinion opinion,  HttpServletResponse response) throws UserNotFoundException {

        boolean is200 = false;
        boolean is205 = false;
        boolean is403 = true;
        String username1 = null;

        try {
            username1 = jwtDecoder.decodeUsernameFromLoginToken(loginToken);
            Opinion existingOpinion = opinionsRepository.findOpinionById(opinion.getId());
            String username2 = existingOpinion.getCollaboratorName();

            if (!userServiceImpl.findUserByUsername(username1).isEnabled() || !userServiceImpl.findUserByUsername(username2).isEnabled()) {
                is403 = true;}
            if (username1.equals(username2)) {
                is403 = false;
            }
            if (Objects.equals(opinion, null)) { //I was caring about nullpointerexception, deleted
                is205 = true;
            }
            is200 = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        finally {
            if (is403) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the initial author is able to modify the content.");
            } else if (is205) {
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Please reset the content");
            } else if (is200) {
                //opinionsRepository.save(opinion);
                customOpinionRepositoryImpl.saveOpinionWithCollaboratorName(opinion,username1);

                Cookie loginCookie = new Cookie("loginToken", loginToken);
                loginCookie.setHttpOnly(true);
                loginCookie.setMaxAge(604800); //a week
                response.setContentType("application/json");
                response.addCookie(loginCookie);

                response.addHeader("Access-Control-Expose-Headers", "loginToken");
                response.addHeader("Access-Control-Max-Age", "604800");//for preflight

                return new ResponseEntity<>("Opinion updated successfully!", HttpStatus.OK);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //delete
    @Transactional
    @DeleteMapping("/opinions/{id}")
    public ResponseEntity<?> deleteOpinion(@CookieValue(name = "loginToken") String loginToken,@PathVariable Long id,HttpServletResponse response) throws UserNotFoundException{

        boolean is204=false;
        boolean is403=false;
        try {
            //verify the user identity first
            String username1 = jwtDecoder.decodeUsernameFromLoginToken(loginToken);
            System.out.println("username1 in deleteOpinions:" + username1);

            Opinion opinion2 = opinionsRepository.findOpinionById(id);
            String username2 = opinion2.getCollaboratorName();

            if (!Objects.equals(username1, username2)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The Collaborator does not correspond with the author of the article in the Database. Please contact us.");
            }
            opinionsRepository.deleteById(id);
            is204=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(is204){
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
                response.addHeader("Access-Control-Expose-Headers", "loginToken");
                response.addHeader("Access-Control-Max-Age", "604800");

                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            }else if(is403){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}