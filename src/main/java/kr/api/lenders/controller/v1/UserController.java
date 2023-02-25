package kr.api.lenders.controller.v1;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.service.UserService;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated // to enable @Valid
public class UserController {
    @NotNull
    private final UserService userService;

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getUser(@PathVariable("id") @Min(1) final long id) {
        return userService.findOne(id);
    }

    /**
     * [TODO]
     *   let client know it's idempotent
     */
    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse updateUser(@Valid @RequestBody final UserUpdateRequest userUpdateRequest) {
        return userService.update(userUpdateRequest);
    }
}
