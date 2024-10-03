package io.digitallly2024.webservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.digitallly2024.webservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class JwtUtils {

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    public String generateToken(User user) {
        return JWT
                .create()
                .withSubject(user.getId().toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(Duration.ofDays(2)))
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .sign(getSigningAlgorithm());
    }

    public DecodedJWT validateToken(String token) {
        JWTVerifier verifier = JWT.require(getSigningAlgorithm()).build();
        return verifier.verify(token);
    }

    private Algorithm getSigningAlgorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

}
