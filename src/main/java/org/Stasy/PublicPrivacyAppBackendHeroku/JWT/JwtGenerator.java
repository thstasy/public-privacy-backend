//package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
//import org.Stasy.PublicPrivacyAppBackendHeroku.User.User;
//import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository.OpinionsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//import java.util.List;
//
//
//
//@Service
//public class JwtGenerator implements JwtGeneratorInterface {
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Autowired
//    OpinionsRepository opinionsRepository;
//
//    //step1 :turn the key to byte
//    private Key getSigningKey() {
//        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    //step2:function to generate Token
//    @Override
//    public String generateLoginToken(User user) throws UnsupportedEncodingException {
//
//        String loginToken = Jwts.builder()
//                .setSubject(user.getEmail())//because when user logs in, we require password ad email
//                .setIssuedAt(new Date())//now
//                .claim("username", user.getUsername())
//                .signWith(getSigningKey())
//                .compact();
//
//        return loginToken;
//    }
//
//    @Override
//    public String generateDashboardToken(User user) {
//
//        List<Opinion> opinionsByUser = opinionsRepository.findOpinionByCollaboratorName(user.getUsername());
//
//        String dashboardToken = Jwts.builder()
//                .setSubject(user.getUsername())
//                .claim("opinionsList", opinionsByUser)
//                .claim("email", user.getEmail())
//                .claim("password", user.getPassword())
//                .claim("username", user.getUsername())
//                .setIssuedAt(new Date())
//                .signWith(getSigningKey())
//                .compact();
//
//        return dashboardToken;
//    }
//}
package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.Stasy.PublicPrivacyAppBackendHeroku.Draft.Draft;
import org.Stasy.PublicPrivacyAppBackendHeroku.Draft.DraftRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.Opinion;
import org.Stasy.PublicPrivacyAppBackendHeroku.Opinion.OpinionRepository.OpinionsRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;



@Service
public class JwtGenerator implements JwtGeneratorInterface {
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    OpinionsRepository opinionsRepository;

    @Autowired
    DraftRepository draftRepository;

    //step1 :turn the key to byte
    private Key getSigningKey() {
        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //step2:function to generate Token
    @Override
    public String generateLoginToken(User user) throws UnsupportedEncodingException {

        String loginToken = Jwts.builder()
                .setSubject(user.getEmail())//because when user logs in, we require password ad email
                .setIssuedAt(new Date())//now
                .claim("username", user.getUsername())
                .signWith(getSigningKey())
                .compact();

        return loginToken;
    }

}