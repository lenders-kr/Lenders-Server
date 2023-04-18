package kr.api.lenders.service;

import jakarta.validation.constraints.NotNull;
import kr.api.lenders.domain.Location;
import kr.api.lenders.domain.LocationRepository;
import kr.api.lenders.error.NotFoundException;
import kr.api.lenders.service.value.LocationResponse;
import kr.api.lenders.util.LocationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    @NotNull
    private final transient LocationRepository locationRepository;

    @NotNull
    private final transient LocationUtil locationUtil;

    public Location findOne(long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location not found"));
    }

    @Transactional
    public LocationResponse findOrCreate(double latitude, double longitude) {
        // Try to find location in the database
        Optional<Location> location = locationRepository.findByLatitudeAndLongitude(latitude, longitude);

        // If location not found, create a new location
        return LocationResponse.of(location.orElseGet(() -> {
            Location newLocation = locationUtil.getLocationFromCoordinates(latitude, longitude);
            // Save new location to the database
            return locationRepository.save(newLocation);
        }));
    }
}
