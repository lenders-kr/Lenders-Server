package kr.api.lenders.config;

import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import kr.api.lenders.error.*;
import org.springframework.core.MethodParameter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.NestedServletException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(final @NotNull MethodParameter returnType,
                            final @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(final Object body,
                                  final @NotNull MethodParameter returnType,
                                  final @NotNull MediaType selectedContentType,
                                  final @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  final @NotNull ServerHttpRequest request,
                                  final @NotNull ServerHttpResponse response) {
        response.getHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return body;
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", NOT_FOUND.value());
        resBody.put("message", NOT_FOUND.getReasonPhrase());

        return resBody;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", BAD_REQUEST.value());
        resBody.put("message", e.getMessage());

        return resBody;
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", BAD_REQUEST.value());
        resBody.put("message", e.getMessage());

        return resBody;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ValidationExceptionDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ValidationExceptionDetail validationExceptionDetail = new ValidationExceptionDetail(BAD_REQUEST.value(), "validation error");
        for (FieldError fieldError : fieldErrors) {
            validationExceptionDetail.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return validationExceptionDetail;
    }

    @ExceptionHandler({NotFoundException.class, NoResultException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleEmptyResultDataAccessException(final Throwable throwable) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", NOT_FOUND.value());
        resBody.put("message", getMessage(throwable));

        return resBody;
    }

    @ExceptionHandler({DuplicationException.class, ParameterValidationException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleBadRequestParameterException(final Throwable throwable) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", BAD_REQUEST.value());
        resBody.put("message", getMessage(throwable));

        return resBody;
    }

    @ExceptionHandler({ForbiddenException.class})
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    public Map<String, Object> handleForbiddenParameterException(final Throwable throwable) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", FORBIDDEN.value());
        resBody.put("message", getMessage(throwable));

        return resBody;
    }

    /**
     * [TODO]
     *   add handler when no request body is present (or consider combining with above handlers)
     */

    private String getMessage(Throwable throwable) {
        if (throwable.getMessage() != null) {
            if (throwable instanceof NestedServletException) {
                return throwable.getCause().getMessage();
            } else {
                return throwable.getMessage();
            }
        }

        return "";
    }
}
