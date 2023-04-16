package kr.api.lenders.controller.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.service.LocationService;
import kr.api.lenders.service.value.LocationFindOrCreateRequest;
import kr.api.lenders.service.value.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "locations", description = "Location API")
@RestController
@RequiredArgsConstructor
@Validated
public class LocationController {
    @NotNull
    private final LocationService locationService;

    /**
     * [TODO] temp method for testing, remove later
     */
    @GetMapping(value = "/locations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LocationResponse getLocation(@PathVariable("id") final long id) {
        return locationService.findOne(id);
    }

    @PostMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public LocationResponse saveLocation(
            @ParameterObject @Valid @RequestBody final LocationFindOrCreateRequest locationFindOrCreateRequest
    ) {
        return locationService.findOrCreate(
                locationFindOrCreateRequest.getLatitude(),
                locationFindOrCreateRequest.getLongitude()
        );
    }
}
