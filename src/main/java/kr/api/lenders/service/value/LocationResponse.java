package kr.api.lenders.service.value;

import kr.api.lenders.domain.Location;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationResponse {
    long id;

    String province;

    String district;

    double latitude;

    double longitude;

    public static LocationResponse of(final Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .province(location.getProvince())
                .district(location.getDistrict())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}
