package kr.api.lenders.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.service.PostService;
import kr.api.lenders.service.value.PostCreateRequest;
import kr.api.lenders.service.value.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "posts", description = "Post API")
@RestController
@RequiredArgsConstructor
@Validated
public class PostController {
    @NotNull
    private final PostService postService;

    @Operation(summary = "Get post", description = "Get post by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            )
    })
    @GetMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostResponse getPost(@PathVariable("id") final long id) {
        return postService.findOne(id);
    }

    @Operation(summary = "Create post", description = "Create post")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            )
    })
    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostResponse savePost(
            @ParameterObject @Valid @RequestBody final PostCreateRequest postCreateRequest,
            final Authentication authentication) {
        final User currentUser = (User) authentication.getPrincipal();
        return postService.save(postCreateRequest, currentUser);
    }
}
