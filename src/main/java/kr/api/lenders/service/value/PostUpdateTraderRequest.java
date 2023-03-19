package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateTraderRequest {
    @Parameter(
            name = "traderId",
            description = "Trader's id (pass null to release trader)",
            required = true,
            schema = @Schema(type = "number", format = "long")
    )
    @Nullable
    Long traderId;
}
