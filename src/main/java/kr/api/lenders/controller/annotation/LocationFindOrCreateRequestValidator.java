package kr.api.lenders.controller.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.api.lenders.service.value.LocationFindOrCreateRequest;

public class LocationFindOrCreateRequestValidator implements ConstraintValidator<ValidateLocation, LocationFindOrCreateRequest> {
    @Override
    public void initialize(ValidateLocation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocationFindOrCreateRequest locationFindOrCreateRequest,
                           ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;
        if (locationFindOrCreateRequest.getLatitude() == null ||
                locationFindOrCreateRequest.getLatitude() > 90 ||
                locationFindOrCreateRequest.getLatitude() < -90) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Latitude must be provided and be between -90 and 90")
                    .addPropertyNode("latitude")
                    .addConstraintViolation();
            isValid = false;
        }

        if (locationFindOrCreateRequest.getLongitude() == null ||
                locationFindOrCreateRequest.getLongitude() > 180 ||
                locationFindOrCreateRequest.getLongitude() < -180) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Longitude must be provided and be between -180 and 180")
                    .addPropertyNode("longitude")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}

