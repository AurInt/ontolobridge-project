package edu.miami.schurer.ontolobridge.utilities;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwtExpiration}")      // in millisecond
    private int jwtExpiration;
    private final Date jwtExpirationTime = new Date((new Date()).getTime() + jwtExpiration);
    private final static SecretKey key = Jwts.SIG.HS256.key().build();  //https://github.com/jwtk/jjwt?tab=readme-ov-file#creating-safe-keys

    // Logic: generateJwtToken creates a new jwt token using the created SecretKey above,
    // which is stored to be used for validation (?) below when Jwts.parser is called.
    // Then when JWTRefreshInterceptor is called, how would the Jwts.parser decrypt using the refresh_key?
    public String generateJwtToken(Authentication authentication) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder()       //https://github.com/jwtk/jjwt?tab=readme-ov-file#detached-payload-example
                .signWith(key)
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(jwtExpirationTime)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseClaimsJws(jwt)
                .getSubject();
    }
    public Date getExpirationFromJWTToken(String token){
        return Jwts.parser()
                .verifyWith(key)
                .parseClaimsJws(token.getToken());
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
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
