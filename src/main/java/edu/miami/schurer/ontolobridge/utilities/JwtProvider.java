package edu.miami.schurer.ontolobridge.utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        SecretKey key = (Jwts.SIG.HS512.key().build());

        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key)
                .content(jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(SecretKey token) {
        return Jwts.parser()
                .verifyWith(token)
                .setSigningKey(jwtSecret)
                .parseSignedClaims(token)
                .getBody().getSubject();
    }
    public Date getExpirationFromJWTToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getExpiration();
    }

    public boolean validateJwtToken(String authToken) throws SignatureException {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token ");
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }
}
