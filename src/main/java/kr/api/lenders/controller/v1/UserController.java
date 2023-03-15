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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.service.UserService;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "User API")
@RestController
@RequiredArgsConstructor
@Validated // to enable @Valid
public class UserController {
    @NotNull
    private final UserService userService;

    @Operation(summary = "Get user", description = "Get user by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = UserResponse.class))
            )
    })
    @Parameters({
            @Parameter(
                    name = "id",
                    description = "User id",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
    })
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getUser(@PathVariable("id") @Min(1) final long id) {
        return userService.findOne(id);
    }

    @Operation(summary = "Update user", description = "Update user (current support: nickname & image)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = UserResponse.class))
            )
    })
    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse updateUser(
            @ParameterObject @Valid @RequestBody final UserUpdateRequest userUpdateRequest,
            final Authentication authentication) { // [TODO] switch to annotation to get current user
        final User currentUser = (User) authentication.getPrincipal();
        return userService.update(currentUser, userUpdateRequest);
    }
}
