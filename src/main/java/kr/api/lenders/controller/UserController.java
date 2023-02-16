package kr.api.lenders.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.service.UserService;
import kr.api.lenders.service.value.UserCreateRequest;
import kr.api.lenders.service.value.UserResponse;
import kr.api.lenders.service.value.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    @NotNull
    private final UserService userService;

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getUser(@PathVariable("id") @Min(1) final long id) {
        return userService.findOne(id);
    }

    /**
     * @deprecated
     * [TODO]
     *   TEMP API method for development process
     *   should only allow social login later
     */
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse createUser(@Valid @RequestBody final UserCreateRequest userCreateRequest) {
        return userService.save(userCreateRequest);
    }

    /**
     * [TODO]
     *   implement auth
     *   let client know it's idempotent
     */
    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse updateUser(@Valid @RequestBody final UserUpdateRequest userUpdateRequest) {
        return userService.update(userUpdateRequest);
    }

    /**
     * [TODO]
     *   implement auth
     *   change path variable to using auth's owner user id
     */
    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse deleteUser(@PathVariable("id") @Min(1) final long id) {
        return userService.delete(id);
    }
}
