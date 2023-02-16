package kr.api.lenders.service.value;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank
    private final String email;

    @NotBlank
    private final String name;

    @NotBlank
    private final String nickname;
}
