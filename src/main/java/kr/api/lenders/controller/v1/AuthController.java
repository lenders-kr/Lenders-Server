package kr.api.lenders.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.domain.UserSsoDetail;
import kr.api.lenders.service.UserService;
import kr.api.lenders.service.UserSsoDetailService;
import kr.api.lenders.service.value.AuthResponse;
import kr.api.lenders.service.value.UserRegisterRequest;
import kr.api.lenders.service.value.UserSocialLoginRequest;
import kr.api.lenders.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "auth", description = "Auth API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    @NotNull
    private final UserService userService;

    @NotNull
    private final UserSsoDetailService userSsoDetailService;

    @Operation(summary = "Register", description = "Register user with email and password")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            )
    })
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse register(@ParameterObject @Valid @RequestBody final UserRegisterRequest userRegisterRequest) {
        User user = userService.register(userRegisterRequest);
        String token = JwtUtil.generateToken(new HashMap<>(), user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Operation(summary = "Login with google", description = "Login with google")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
            )
    })
    @PostMapping(value = "/login/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResponse googleLogin(@ParameterObject @Valid @RequestBody final UserSocialLoginRequest userSocialLoginRequest) {
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
