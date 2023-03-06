package kr.api.lenders.error;

public class GoogleAuthenticationException extends RuntimeException {
    public GoogleAuthenticationException() {
        super("Invalid Google token.");
    }
}
