package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @Parameter(
            name = "id",
            description = "User id",
            required = true,
            example = "1",
            schema = @Schema(type = "integer", format = "int64")
    )
    @NotNull
    private long id;

    @Parameter(
            name = "nickname",
            description = "User nickname (length: 1 ~ 20)",
            example = "test nickname 1 2 3",
            schema = @Schema(type = "string", format = "string")
    )
    @Length(min = 1, max = 20)
    private String nickname;

    @Parameter(
            name = "image",
            description = "User image (url)",
            example = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
            schema = @Schema(type = "string", format = "string")
    )
    private String image;
}
