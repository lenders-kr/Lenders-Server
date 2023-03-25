package kr.api.lenders.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.User;
import kr.api.lenders.service.ReviewService;
import kr.api.lenders.service.value.ReviewCreateRequest;
import kr.api.lenders.service.value.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "reviews", description = "Review API")
@RestController
@RequiredArgsConstructor
@Validated
public class ReviewController {
    @NotNull
    private final ReviewService reviewService;

    @Operation(summary = "Create review", description = "Create review")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    useReturnTypeSchema = true
            )
    })
    @PostMapping(value = "/review", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReviewResponse save(
            @ParameterObject @Valid @RequestBody final ReviewCreateRequest reviewCreateRequest,
            final Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();
        return reviewService.save(currentUser, reviewCreateRequest);
    }

    @Operation(summary = "Get review", description = "Get review")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    useReturnTypeSchema = true
            )
    })
    @GetMapping(value = "/reviews/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReviewResponse getReview(@PathVariable("id") @Min(1) final long id) {
        return reviewService.findOne(id);
    }
}
