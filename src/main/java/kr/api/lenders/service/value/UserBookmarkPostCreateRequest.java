package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBookmarkPostCreateRequest {
    @Parameter(
            name = "postId",
            description = "Post id",
            required = true,
            schema = @Schema(type = "long", format = "int64")
    )
    @Min(1)
    private long postId;
}
