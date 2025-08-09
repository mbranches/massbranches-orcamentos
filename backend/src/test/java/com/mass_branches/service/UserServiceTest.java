package com.mass_branches.service;

import com.mass_branches.dto.request.LoginRequest;
import com.mass_branches.dto.response.LoginResponse;
import com.mass_branches.dto.response.UserGetResponse;
import com.mass_branches.infra.security.JwtTokenService;
import com.mass_branches.model.User;
import com.mass_branches.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private Authentication authentication;
    private List<User> userList;
    private List<UserGetResponse> userGetResponseList;

    @BeforeEach
    void init() {
        userList = UserUtils.newUserList();
        userGetResponseList = UserUtils.newUserGetResponseList();
    }

    @Test
    @DisplayName("login returns jwtT token when successful")
    @Order(1)
    void login_ReturnsJwtToken_WhenSuccessful() {
        LoginRequest request = UserUtils.newLoginRequest();
        LoginResponse expectedResponse = UserUtils.newLoginResponse();

        User user = userList.getFirst();

        when(authenticationManager.authenticate(ArgumentMatchers.any()))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(user);
        when(jwtTokenService.generateToken(user))
                .thenReturn(expectedResponse.accessToken());


        LoginResponse response = service.login(request);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("getMe returns the given user datas when successful")
    @Order(2)
    void getMe_ReturnsTheGivenUserDatas_WhenSuccessful() {
        User userToGetDatas = userList.getFirst();

        UserGetResponse expectedResponse = userGetResponseList.getFirst();

        UserGetResponse response = service.getMe(userToGetDatas);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }
}