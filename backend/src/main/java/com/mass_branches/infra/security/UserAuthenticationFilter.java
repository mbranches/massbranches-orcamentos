package com.mass_branches.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mass_branches.model.User;
import com.mass_branches.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository repository;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoveryToken(request);

        if (token != null) {
            try {
                String subject = jwtTokenService.validateToken(token);

                User user = repository.findById(subject)
                        .orElseThrow(() -> new BadCredentialsException(null));

                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JWTVerificationException ignored) {}
        }

        filterChain.doFilter(request, response);
    }

    public String recoveryToken(HttpServletRequest request) {
        String authentication = request.getHeader("Authorization");

        if (authentication != null) return authentication.replace("Bearer ", "");

        return null;
    }
}
