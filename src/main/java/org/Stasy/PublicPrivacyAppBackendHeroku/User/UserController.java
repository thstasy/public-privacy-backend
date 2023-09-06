package org.Stasy.PublicPrivacyAppBackendHeroku.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.Draft.CustomDraftRepositoryImpl;
import org.Stasy.PublicPrivacyAppBackendHeroku.Draft.Draft;
import org.Stasy.PublicPrivacyAppBackendHeroku.Draft.DraftRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Entity.ConfirmationToken;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.ConfirmationTokenRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.UserRepository2;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service.EmailService;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service.UserService2;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtDecoder;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtGenerator;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtGeneratorInterface;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository.OpinionsRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserAlreadyExistsException;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.Stasy.PublicPrivacyAppBackendHeroku.JWT.passwordVerifier.verifyPassword;

@RestController
@RequestMapping(value="/collaborator")
public class UserController {

    private String globalLoginToken;
    private final JwtGenerator jwtGenerator;
    private final UserServiceImpl userServiceImpl;
    private final UserServiceInterface userServiceInterface;
    private final JwtGeneratorInterface jwtGeneratorInterface;
    private final JwtDecoder jwtDecoder;

    private final CustomDraftRepositoryImpl customDraftRepository;

    private final OpinionsRepository opinionsRepository;

    private final DraftRepository draftRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.Domain}")
    private String homepage;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    UserService2 userService2;
    @Autowired
    UserRepository2 userRepository2;
    private Exception UserNotFoundException;

    private void setLoginTokenCookie(HttpServletResponse response, String loginToken) {
        Cookie loginCookie = new Cookie("loginToken", loginToken);
        loginCookie.setHttpOnly(true);
        loginCookie.setMaxAge(604800); //java counts in seconds, so a week is
        response.addCookie(loginCookie);
    }

    //constructor
    @Autowired
    public UserController(JwtGenerator jwtGenerator, UserServiceImpl userServiceImpl, UserServiceInterface userServiceInterface, JwtGeneratorInterface jwtGeneratorInterface, JwtDecoder jwtDecoder, CustomDraftRepositoryImpl customDraftRepository, OpinionsRepository opinionsRepository, DraftRepository draftRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userServiceImpl = userServiceImpl;
        this.userServiceInterface = userServiceInterface;
        this.jwtGeneratorInterface = jwtGeneratorInterface;
        this.jwtDecoder=jwtDecoder;
        this.customDraftRepository = customDraftRepository;
        this.opinionsRepository=opinionsRepository;
        this.draftRepository = draftRepository;
    }

    //all methods
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, HttpServletResponse response) throws UserNotFoundException, UserAlreadyExistsException
    {
        boolean isSaved=false;

        if(!Objects.equals(userServiceInterface.findUserByUsername(user.getUsername()),null)){
            return new ResponseEntity<>("User Already Exists", HttpStatus.CONFLICT);//409
        }
        if(!Objects.equals(userServiceInterface.findUserByEmail(user.getEmail()),null)){
            return new ResponseEntity<>("User Already Exists", HttpStatus.CONFLICT);//409
        }
        if(Objects.equals(user.getUsername(),null) || Objects.equals(user.getEmail(),null) || Objects.equals(user.getPassword(),null)){
            return new ResponseEntity<>("The fields are required to be filled", HttpStatus.CONFLICT);
        }

        try{
            String password=user.getPassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now()); // Set the created date to the current time
            userServiceInterface.save(user);
            isSaved=true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(isSaved){

                //send verification token
                ConfirmationToken confirmationToken = new ConfirmationToken(user);
                confirmationTokenRepository.save(confirmationToken);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Registration Confirmation");
                mailMessage.setText("To confirm your account, please click here : "
                        +baseUrl+"confirm-account?token="+confirmationToken.getConfirmationToken());
                emailService.sendEmail(mailMessage);

                return ResponseEntity.status(HttpStatus.CREATED).build();//201
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //500
            }
        }
    }

    //active the account
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        //this is the part of confirm email
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = userRepository2.findByEmailIgnoreCase(token.getUserEntity().getEmail());
            user.setEnabled(true);
            userRepository2.save(user);
            String homepageLink = "<a href=\"" + homepage + "\">Homepage</a>";
            String message = "Email verified successfully! Head to " + homepageLink + ".";

            return ResponseEntity.ok(message);
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email. Please try again with the address.");
    }

    @PostMapping("/login") //login using email and password
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) throws UserNotFoundException
    {
        boolean isExist = false;
        boolean isActivated=false;
        String loginToken=null;


        String email = user.getEmail();//this cannot be directly reflected upon deleting. I can still fetch ID.
        String enteredPassword = user.getPassword();
        User user_db = userServiceImpl.findUserByEmail(email); // Retrieve the hashed password from the backend based on the user's email
        String hashedPassword = user_db.getPassword();

        try {
            //check is Enabled
            if (!user_db.isEnabled()) {
                isActivated = false;
            } else {
                isActivated = true;
            }

            //check is email valid
            try {
                if (Objects.equals((userServiceImpl.findUserByEmail(user.getEmail())), null)) {
                    isExist = false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            //check is password valid
            try {
                if (verifyPassword(enteredPassword, user_db.getPassword())) {
                    isExist = true;
                }else{
                    isExist = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }finally {
            if (!isExist || !isActivated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();//400
            }else if (isExist && isActivated){
                try {

//                    globalLoginToken = jwtGenerator.generateLoginToken(user_db);
//                    setLoginTokenCookie(response, globalLoginToken);

                    loginToken = jwtGenerator.generateLoginToken(user_db);
                    Cookie loginCookie = new Cookie("loginToken", loginToken);
                    loginCookie.setHttpOnly(true);
                    loginCookie.setMaxAge(604800); //java counts in seconds, so a week is
                    response.setContentType("application/json");
                    response.addCookie(loginCookie);
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                response.addHeader("Access-Control-Expose-Headers", "loginToken");
                response.addHeader("Access-Control-Max-Age", "604800");

                return ResponseEntity.ok().body(loginToken);
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //500
            }
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getUserInfo(@CookieValue(name = "loginToken", required = true) String loginToken, HttpServletResponse response) throws UserNotFoundException {
        boolean isActivated;

        String email = jwtDecoder.decodeUserEmailFromLoginToken(loginToken);
        User user = userServiceImpl.findUserByEmail(email);

        if (!user.isEnabled()) {
            return new ResponseEntity<>("The Account is not activated. Please validate through the email. ", HttpStatus.FORBIDDEN); // 403
        } else {
            isActivated = true;
        }

        if (Objects.equals(loginToken, null)) {
            return new ResponseEntity<>("Login Token is required.", HttpStatus.RESET_CONTENT); // 205
        } else if (Objects.equals(jwtDecoder.decodeUserEmailFromLoginToken(loginToken), null)) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN); // 403
        } else if (Objects.equals(userServiceImpl.findUserByEmail(jwtDecoder.decodeUserEmailFromLoginToken(loginToken)), null)) {
            return new ResponseEntity<>("The Input information from login Token might be incorrect.", HttpStatus.RESET_CONTENT); // 205
        }

        List<Opinion> opinionsByUser = opinionsRepository.findOpinionByCollaboratorNameOrderByUpdatedAtDesc(user.getUsername());
        List<Draft> draftsByUser = draftRepository.findAllByCollaboratorNameOrderByUpdatedAtDesc(user.getUsername());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseBody = objectMapper.createObjectNode();

        ArrayNode opinionsArray = objectMapper.valueToTree(opinionsByUser);
        ArrayNode draftsArray = objectMapper.valueToTree(draftsByUser);

        responseBody.set("opinionsByUser", opinionsArray);
        responseBody.set("draftsByUser", draftsArray);

        response.addHeader("Access-Control-Expose-Headers", "loginToken");
        response.addHeader("Access-Control-Max-Age", "604800");
        response.addCookie(new Cookie("loginToken", loginToken));


        if (isActivated) {
            response.addHeader("Access-Control-Expose-Headers", "loginToken");
            response.addHeader("Access-Control-Max-Age", "604800");
            response.addCookie(new Cookie("loginToken", loginToken));

            return ResponseEntity.ok().body(responseBody); // 200
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
    }

    @Transactional
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(@CookieValue(name = "loginToken", required = true) String loginToken,@RequestParam("username") String username, HttpServletResponse response)
    {

        try {
            User user=userServiceImpl.findUserByUsername(username);

            System.out.println(user);

            if (Objects.equals(user,null)){
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();//user Not found
            }

            if (!user.isEnabled()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();//403
            }

            confirmationTokenRepository.deleteByUserId(user.getUserID());

            userServiceImpl.deleteByUsername(username);

            opinionsRepository.deleteAllByCollaboratorName(username);

            System.out.println(user);

            response.addHeader("Access-Control-Expose-Headers", "loginToken");
            response.addHeader("Access-Control-Max-Age", "604800");

            return ResponseEntity.status(HttpStatus.OK).build(); // 200

        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
