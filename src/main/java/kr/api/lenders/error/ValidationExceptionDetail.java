package kr.api.lenders.error;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationExceptionDetail {
    private final int status;
    private final String message;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public ValidationExceptionDetail(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void addFieldError(String field, String defaultMessage) {
        FieldError error = new FieldError(field, defaultMessage);
        fieldErrors.add(error);
    }

    static class FieldError {
        private final String field;
        private final String message;

        FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return this.field;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
