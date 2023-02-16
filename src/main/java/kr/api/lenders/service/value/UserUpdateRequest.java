package kr.api.lenders.service.value;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private long id;

    @NotNull
    @Length(min = 1, max = 20)
    private String nickname;

    private String image;
}
