package kr.api.lenders.service.value;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.type.UserSsoProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserSocialLoginRequest {
    @NotNull
    /**
     * [TODO]
     *   add enum annotation
     */
    private UserSsoProviderType providerType;

    @NotNull
    private String identifier;
}
