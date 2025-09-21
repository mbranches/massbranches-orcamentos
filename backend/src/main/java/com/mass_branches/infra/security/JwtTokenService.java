package com.mass_branches.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mass_branches.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
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
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
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
        return Instant.now();
    }

    public Instant expirationDate() {
        long seconds = 20 * 60 * 60;
        return Instant.now().plusSeconds(seconds);
    }
}
