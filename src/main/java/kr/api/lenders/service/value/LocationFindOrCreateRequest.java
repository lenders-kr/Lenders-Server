package kr.api.lenders.service.value;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.api.lenders.controller.annotation.ValidateLocation;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@ValidateLocation
public class LocationFindOrCreateRequest {
    @Parameter(
            name = "latitude",
            description = "Latitude of post (between -90 and 90)",
            required = true,
            schema = @Schema(type = "number", format = "double")
    )
    Double latitude;

    @Parameter(
            name = "longitude",
            description = "Longitude of post (between -180 and 180)",
            required = true,
            schema = @Schema(type = "number", format = "double")
    )
    Double longitude;
}
