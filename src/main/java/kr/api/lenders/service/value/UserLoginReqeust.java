package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserLoginReqeust {
    @Parameter(
            name = "email",
            description = "User email",
            required = true,
            schema = @Schema(type = "string", format = "email")
    )
    @NotBlank
    String email;

    @Parameter(
            name = "password",
            description = "User password",
            required = true,
            schema = @Schema(type = "string", format = "password")
    )
    @NotBlank
    String password;
}
