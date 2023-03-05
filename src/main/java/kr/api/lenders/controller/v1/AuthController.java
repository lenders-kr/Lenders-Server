package kr.api.lenders.controller.v1;

import jakarta.validation.Valid;
import kr.api.lenders.domain.User;
import kr.api.lenders.service.UserService;
import kr.api.lenders.service.value.AuthResponse;
import kr.api.lenders.service.value.UserRegisterRequest;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserSocialLoginRequest;
import kr.api.lenders.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final UserService userService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse register(@Valid @RequestBody final UserRegisterRequest userRegisterRequest) {
        User user = userService.register(userRegisterRequest);
        String token = JwtUtil.generateToken(new HashMap<>(), user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    /**
     * [TODO]
     *   change response to AuthResponse
     */
    @PostMapping(value = "/login/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse googleLogin(@Valid @RequestBody final UserSocialLoginRequest userSocialLoginRequest) {
        return userService.socialLogin(userSocialLoginRequest);
    }
}
