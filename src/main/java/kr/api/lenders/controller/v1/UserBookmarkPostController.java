package kr.api.lenders.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.service.UserBookmarkPostService;
import kr.api.lenders.service.value.OkResponse;
import kr.api.lenders.service.value.UserBookmarkPostCreateRequest;
import kr.api.lenders.service.value.UserBookmarkPostResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user-bookmark-post", description = "User bookmark post API")
@RestController
@RequiredArgsConstructor
@Validated
public class UserBookmarkPostController {
    @NotNull
    private final UserBookmarkPostService userBookmarkPostService;

    @Operation(summary = "Create user bookmark post", description = "Create user bookmark post")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    useReturnTypeSchema = true
            )
    })
    @PostMapping(value = "/user-bookmark-post", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserBookmarkPostResponse save(
            @ParameterObject @Valid @RequestBody final UserBookmarkPostCreateRequest userBookmarkPostCreateRequest,
            final Authentication authentication
    ) {
        final User currentUser = (User) authentication.getPrincipal();
        return userBookmarkPostService.save(currentUser, userBookmarkPostCreateRequest);
    }

    @Operation(summary = "Remove user bookmark post", description = "Remove user bookmark post")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    useReturnTypeSchema = true
            )
    })
    @DeleteMapping(value = "/user-bookmark-post/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OkResponse remove(
            @PathVariable("id") final long id,
            final Authentication authentication
    ) {
        final User currentUser = (User) authentication.getPrincipal();
        return userBookmarkPostService.remove(currentUser, id);
    }

    @Operation(summary = "Get user bookmark posts", description = "Get user bookmark posts")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    useReturnTypeSchema = true
            )
    })
    @GetMapping(value = "/user-bookmark-posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<UserBookmarkPostResponse> getUserBookmarkPosts(
            final Authentication authentication,
            @ParameterObject @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        final User currentUser = (User) authentication.getPrincipal();
        return userBookmarkPostService.findAllByUser(currentUser, pageable);
    }
}
