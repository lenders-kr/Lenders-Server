package kr.api.lenders.controller.v1;

import jakarta.validation.Valid;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.UserSsoDetail;
import kr.api.lenders.service.UserService;
import kr.api.lenders.service.UserSsoDetailService;
import kr.api.lenders.service.value.AuthResponse;
import kr.api.lenders.service.value.UserRegisterRequest;
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
    private final UserSsoDetailService userSsoDetailService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse register(@Valid @RequestBody final UserRegisterRequest userRegisterRequest) {
        User user = userService.register(userRegisterRequest);
        String token = JwtUtil.generateToken(new HashMap<>(), user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @PostMapping(value = "/login/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse googleLogin(@Valid @RequestBody final UserSocialLoginRequest userSocialLoginRequest) {
        UserSsoDetail userSsoDetail = userSsoDetailService.findOrCreate(
                userSocialLoginRequest.getProviderType(),
                userSocialLoginRequest.getToken()
        );
        String token = JwtUtil.generateToken(new HashMap<>(), userSsoDetail.getUser());
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
