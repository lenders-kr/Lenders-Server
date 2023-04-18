package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.api.lenders.controller.annotation.ValueOfEnum;
import kr.api.lenders.domain.type.PostCategoryType;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Value
@AllArgsConstructor
public class PostCreateRequest {
    @Parameter(
            name = "title",
            description = "Title of post (1 ~ 50 characters)",
            required = true,
            schema = @Schema(type = "string", format = "string")
    )
    @Length(min = 1, max = 50)
    String title;

    @Parameter(
            name = "description",
            description = "Description of post (10 ~ 1000 characters)",
            required = true,
            schema = @Schema(type = "string", format = "string")
    )
    @Length(min = 10, max = 1000)
    String description;

    @Parameter(
            name = "price",
            description = "Price of post",
            required = true,
            schema = @Schema(type = "number", format = "double")
    )
    @Min(0)
    double price;

    @Parameter(
            name = "category",
            description = "Category of post",
            required = true,
            schema = @Schema(implementation = PostCategoryType.class)
    )
    @ValueOfEnum(enumClass = PostCategoryType.class, message = "Invalid category type")
    String category;

    @Parameter(
            name = "currency",
            description = "Currency of post",
            required = true,
            schema = @Schema(type = "string", format = "string")
    )
    @NotBlank
    String currency;

    @Parameter(
            name = "images",
            description = "Images of post (min 1, max 10)",
            required = true,
            schema = @Schema(type = "array", format = "string")
    )
    @Size(min = 1, max = 10)
    List<String> images;

    @Parameter(
            name = "locationId",
            description = "Location ID of post",
            required = true,
            schema = @Schema(type = "number", format = "long")
    )
    @Min(1)
    long locationId;
}
