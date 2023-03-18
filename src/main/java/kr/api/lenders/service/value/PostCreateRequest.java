package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import kr.api.lenders.controller.annotation.ValueOfEnum;
import kr.api.lenders.domain.type.PostCategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
public class PostCreateRequest {
    @Parameter(
            name = "title",
            description = "Title of post (1 ~ 50 characters)",
            required = true,
            schema = @Schema(type = "string", format = "string")
    )
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;

    @Parameter(
            name = "description",
            description = "Description of post (10 ~ 1000 characters)",
            required = true,
            schema = @Schema(type = "string", format = "string")
    )
    @NotBlank
    @Length(min = 10, max = 1000)
    private String description;

    @Parameter(
            name = "price",
            description = "Price of post",
            required = true,
            schema = @Schema(type = "number", format = "double")
    )
    @PositiveOrZero
    private double price;

    @Parameter(
            name = "category",
            description = "Category of post",
            required = true,
            schema = @Schema(implementation = PostCategoryType.class)
    )
    @NotNull
    @ValueOfEnum(enumClass = PostCategoryType.class, message = "Invalid category type")
    private String category;

    @Parameter(
            name = "currency",
            description = "Currency",
            required = true,
            schema = @Schema(type = "string", format = "string"),
            example = "KRW"
    )
    @NotBlank
    private String currency;

    /**
     * [TODO] add location field
     */

}
