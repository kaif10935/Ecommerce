package com.springboot.Ecommerce.security.jwt;

import com.springboot.Ecommerce.security.sevices.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.application.secret}")
    private String secret;

    @Value("${spring.application.expiration}")
    private int jwtExpirationMs;

    public String getTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
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
