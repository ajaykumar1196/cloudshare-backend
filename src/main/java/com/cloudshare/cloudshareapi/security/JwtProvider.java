package com.cloudshare.cloudshareapi.security;

import com.cloudshare.cloudshareapi.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;

import static java.util.Date.from;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        SecretKey secretKey =  Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(from(Instant.now()))
                .claim("authorities", user.getAuthorities())
                .signWith(secretKey)
                .setExpiration(java.sql.Date.from(Instant.now().plusMillis(jwtExpirationInMs)))
                .compact();
    }

    public String getSubjectFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public int getJwtExpirationInMs(){
        return jwtExpirationInMs;
    }
}