package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.type.UserSsoProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserSocialLoginRequest {
    @Parameter(
            name = "providerType",
            description = "Social login provider type",
            required = true,
            example = "GOOGLE",
            schema = @Schema(implementation = UserSsoProviderType.class)
    )
    @NotNull
    private UserSsoProviderType providerType;

    @Parameter(
            name = "token",
            description = "Id Token received from social login provider",
            required = true,
            example = "abcd.efgh.ijkl",
            schema = @Schema(type = "string", format = "string")
    )
    @NotNull
    private String token;
}
