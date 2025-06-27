package com.mass_branches.service;

import com.mass_branches.dto.response.LoginResponse;
import com.mass_branches.dto.request.LoginRequest;
import com.mass_branches.dto.response.UserGetResponse;
import com.mass_branches.infra.security.JwtTokenService;
import com.mass_branches.model.User;
import com.mass_branches.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) authentication.getPrincipal();

        return new LoginResponse(jwtTokenService.generateToken(user));
    }

    public UserGetResponse getMe(User requestingUser) {
        return UserGetResponse.by(requestingUser);
    }
}
