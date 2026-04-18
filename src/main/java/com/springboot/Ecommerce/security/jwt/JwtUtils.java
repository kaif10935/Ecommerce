package com.springboot.Ecommerce.security.jwt;

import com.springboot.Ecommerce.security.sevices.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.application.secret}")
    private String secret;

    @Value("${spring.application.expiration}")
    private int jwtExpirationMs;

    @Value("${spring.application.jwtCookie}")
    private String jwtCookie;

//    public String getTokenFromHeader(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }else{
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails){
        String jwt = generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie , jwt)
                                            .path("/api")
                                            .maxAge(60 * 60)
                                            .httpOnly(false)
                                            .build();
        return cookie;
    }

    public ResponseCookie generateCleanCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie , null)
                .path("/api")
                .build();
        return cookie;
    }

    public String getUsernameFromToken(String token) {
       return Jwts
                .parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Key key(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateTokenFromUsername(String username) {
        return Jwts
                .builder()
                .signWith(key())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .subject(username)
                .compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwt);
            return true;
        } catch (JwtException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
