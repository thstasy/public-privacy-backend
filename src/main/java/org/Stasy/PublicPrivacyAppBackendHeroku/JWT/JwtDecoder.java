//package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Header;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.Stasy.PublicPrivacyAppBackendHeroku.Draft.Location;
//
//
//@Component
//public class JwtDecoder {
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    public String decodeUserEmailFromLoginToken(String loginToken) { //when Login, I use email and password. So loginToken contains Email and Password. Now I want to decode it. So
//
//        Jws<Claims> claimsJws = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(loginToken);
//
//        Claims claims = claimsJws.getBody();
//        String result=claimsJws.getBody().getSubject();//subject should be email
//        return result;
//    }
//
//    public Location decodeLocationFromOpinionLoginToken(String loginToken) {
//        Jws<Claims> claimsJws = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(loginToken);
//
//        Claims claims = claimsJws.getBody();
//
//        Location location = claims.get("location", Location.class);
//        return location;
//    }
//
//    public String decodeUsernameFromLoginToken(String loginToken) {
//
//        Jws<Claims> claimsJws = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(loginToken);
//
//        Claims claims = claimsJws.getBody();
//       //yes it's username
//        return (String) claimsJws.getBody().get("username");
//    }
//
//
//    public String decodeUserInfoFromDashboardToken(String dashboardToken) { //when edit/delete, I use email and password. So dashboardToken contains Email and Password and messages.
//
//        Jws<Claims> claimsJws = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(dashboardToken);
//
//        Header headers = claimsJws.getHeader();
//        Claims claims = claimsJws.getBody();
//
//        String subject =claimsJws.getBody().getSubject();
//        return subject;
//    }
//
//    public Location decodeLocationFromOpinionLoginToken(String loginToken) {
//        Jws<Claims> claimsJws = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(loginToken);
//
//        Claims claims = claimsJws.getBody();
//
//        Location location = claims.get("location", Location.class);
//        return location;
//    }
//
//}
//
package org.Stasy.PublicPrivacyAppBackendHeroku.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtDecoder {
    @Value("${jwt.secret}")
    private String secretKey;

    public String decodeUserEmailFromLoginToken(String loginToken) { //when Login, I use email and password. So loginToken contains Email and Password. Now I want to decode it. So

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(loginToken);

        Claims claims = claimsJws.getBody();
        String result=claimsJws.getBody().getSubject();//subject should be email
        return result;
    }


    public String decodeUsernameFromLoginToken(String loginToken) {

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(loginToken);

        Claims claims = claimsJws.getBody();
        //yes it's username
        return (String) claimsJws.getBody().get("username");
    }

}

