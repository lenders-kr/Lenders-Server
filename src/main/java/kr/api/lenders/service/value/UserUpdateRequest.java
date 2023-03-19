package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    @Parameter(
            name = "nickname",
            description = "User nickname (length: 1 ~ 20)",
            example = "test nickname 1 2 3",
            required = true,
            schema = @Schema(type = "string", format = "string")
    )
    @Length(min = 1, max = 20)
    @NotBlank
    private String nickname;

    @Parameter(
            name = "image",
            description = "User image (url)",
            example = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
            schema = @Schema(type = "string", format = "string")
    )
    private String image;
}
