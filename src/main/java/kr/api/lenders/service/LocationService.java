package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.Location;
import kr.api.lenders.domain.LocationRepository;
import kr.api.lenders.util.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    @NotNull
    private final transient LocationRepository locationRepository;

    @NotNull
    private final transient LocationUtil locationUtil;

    public Location findOrCreate(double latitude, double longitude) {
        Optional<Location> location = locationRepository.findByLatitudeAndLongitude(latitude, longitude);
        if (location.isPresent()) {
            return location.get();
        }

        Location newLocation = locationUtil.getLocationFromCoordinates(latitude, longitude);
        return locationRepository.save(newLocation);
    }
}
