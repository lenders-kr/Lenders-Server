package kr.api.lenders.controller.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {LocationFindOrCreateRequestValidator.class})
public @interface ValidateLocation {
    String message() default "Latitude and Longitude must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
