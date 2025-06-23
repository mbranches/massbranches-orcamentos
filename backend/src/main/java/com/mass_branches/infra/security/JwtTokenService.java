package com.mass_branches.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mass_branches.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtTokenService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withIssuer("massbranches-backend")
                .withIssuedAt(creationDate())
                .withExpiresAt(expirationDate())
                .withSubject(user.getId())
                .sign(algorithm);
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.require(algorithm)
                .withIssuer("massbranches-backend")
                .build()
                .verify(token)
                .getSubject();
    }

    public Instant creationDate() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"));
    }

    public Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
