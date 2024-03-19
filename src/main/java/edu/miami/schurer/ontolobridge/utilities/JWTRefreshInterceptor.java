package edu.miami.schurer.ontolobridge.utilities;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JWTRefreshInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JWTRefreshInterceptor.class);
    private JwtProvider tokenProvider;


    @Value("${app.jwtExpiration}")
    private int jwtExpiration;

//cannot modify in post handle so do it here.
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String jwt = getJwt(request);
        if (jwt != null && tokenProvider.validateJwtToken(jwt)) {

            // TODO: factor out Jwts.builder, duplicated in JwtProvider and below
            // FIXME How does refresh_key qualify from JwtProvider?
            SecretKey refresh_key = Jwts.SIG.HS256.key().build();
//TODO: Add in code to save to database to allow revocation
            Date Expiration = tokenProvider.getExpirationFromJWTToken(jwt);
            if ((Expiration.getTime() - new Date().getTime()) < 87000) {
                String token = Jwts.builder()
                        .subject(tokenProvider.getUserNameFromJwtToken(jwt))
                        .issuedAt(new Date())
                        .expiration(new Date((new Date()).getTime() + jwtExpiration))
                        .signWith(refresh_key)
                        .compact();
                response.addHeader("Authorization", "Bearer " + token);
                System.out.println("Refreshing Token");
            }
        }
        return  true;
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ","");
        }

        return null;
    }
}

