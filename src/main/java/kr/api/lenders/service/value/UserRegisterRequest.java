package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserRegisterRequest {

    @Parameter(
            name = "email",
            description = "User email",
            required = true,
            schema = @Schema(type = "string", format = "email")
    )
    @NotBlank
    private String email;

    @Parameter(
            name = "password",
            description = "User password",
            required = true,
            schema = @Schema(type = "string", format = "password")
    )
    @NotBlank
    private String password;

    @Parameter(
            name = "name",
            description = "User name",
            required = true,
            schema = @Schema(type = "string", format = "string")
    )
    @NotBlank
    private String name;
}
