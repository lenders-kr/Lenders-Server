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
import kr.api.lenders.service.value.PostCreateOrUpdateRequest;
import kr.api.lenders.service.value.PostResponse;
import kr.api.lenders.service.value.PostUpdateTraderRequest;
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
            @ParameterObject @Valid @RequestBody final PostCreateOrUpdateRequest postCreateOrUpdateRequest,
            final Authentication authentication) {
        final User currentUser = (User) authentication.getPrincipal();
        return postService.save(postCreateOrUpdateRequest, currentUser);
    }

    @Operation(summary = "Update post", description = "Update post")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            )
    })
    @PutMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostResponse updatePost(
            @PathVariable("id") final long id,
            @ParameterObject @Valid @RequestBody final PostCreateOrUpdateRequest postCreateOrUpdateRequest,
            final Authentication authentication) {
        final User currentUser = (User) authentication.getPrincipal();
        return postService.update(id, postCreateOrUpdateRequest, currentUser);
    }

    @Operation(summary = "Update post trader", description = "Update post trader")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            )
    })
    @PutMapping(value = "/posts/{id}/trader", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostResponse updatePostTrader(
            @PathVariable("id") final long id,
            @ParameterObject @Valid @RequestBody final PostUpdateTraderRequest postUpdateTraderRequest,
            final Authentication authentication) {
        System.out.println("postUpdateTraderRequest = " + postUpdateTraderRequest);
        final User currentUser = (User) authentication.getPrincipal();
        return postService.updateTrader(id, currentUser, postUpdateTraderRequest);
    }

    @Operation(summary = "Remove post", description = "Remove post by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))
            )
    })
    @DeleteMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PostResponse removePost(@PathVariable("id") final long id,
                           final Authentication authentication) {
        final User currentUser = (User) authentication.getPrincipal();
        return postService.remove(id, currentUser);
    }
}
