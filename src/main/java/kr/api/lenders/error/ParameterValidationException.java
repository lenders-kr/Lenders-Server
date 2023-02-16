package kr.api.lenders.error;

public class ParameterValidationException extends RuntimeException {
    public ParameterValidationException(String message) {
        super(message);
    }
}
