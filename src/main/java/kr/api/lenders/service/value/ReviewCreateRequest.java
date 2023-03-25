package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class ReviewCreateRequest {
    @Parameter(
            name = "postId",
            description = "Post id",
            required = true,
            schema = @Schema(type = "long", format = "int64")
    )
    @Min(1)
    private long postId;

    @Parameter(
            name = "content",
            description = "Review content (min 5, max 1000 characters)",
            required = true,
            schema = @Schema(type = "string", format = "text")
    )
    @NotBlank
    @Length(min = 5, max = 1000)
    private String content;

    @Parameter(
            name = "rating",
            description = "Rating (1-5)",
            required = true,
            schema = @Schema(type = "int", format = "int8")
    )
    @Min(1)
    @Max(5)
    private byte rating;
}
