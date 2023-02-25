package kr.api.lenders.controller.v1;

import jakarta.validation.Valid;
import kr.api.lenders.service.UserService;
import kr.api.lenders.service.value.UserRegisterRequest;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserSocialLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated // to enable @Valid
public class AuthController {
    private final UserService userService;

    /**
     * [TODO]
     *   change response to jwt token
     */
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse register(@Valid @RequestBody final UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest);
    }

    /**
     * [TODO]
     *   change response to jwt token
     */
    @PostMapping(value = "/login/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse googleLogin(@Valid @RequestBody final UserSocialLoginRequest userSocialLoginRequest) {
        return userService.socialLogin(userSocialLoginRequest);
    }
}
