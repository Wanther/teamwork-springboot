package teamwork.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<Object> handleAppException(Exception ex, WebRequest request) throws Exception {
        if (ex instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) ex, null, request);
        }

        if (ex instanceof ValidationException) {
            return handleValidationException((ValidationException)ex, null, request);
        }

        return super.handleException(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (ex.hasFieldErrors()) {
            // FieldError::getField, Collectors.mapping(messageSource.getMessage(FieldError::getCode), Collectors.toSet())
            Map<String, Set<String>> body = ex.getFieldErrors().stream().collect(
                    Collectors.groupingBy(FieldError::getField,
                            Collectors.mapping(o -> messageSource.getMessage(o, request.getLocale()), Collectors.toSet())));
            return super.handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
        }

        return super.handleExceptionInternal(ex, messageSource.getMessage(ex.getGlobalError(), request.getLocale()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (ex.getBindingResult().hasFieldErrors()) {
            Map<String, Set<String>> body = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())));
            return super.handleExceptionInternal(ex, body, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
        }

        return super.handleExceptionInternal(ex, messageSource.getMessage(ex.getBindingResult().getGlobalError(), request.getLocale()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return super.handleExceptionInternal(ex, Map.of(ex.getParameterName(), Set.of(messageSource.getMessage("required", null, request.getLocale()))), headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpHeaders headers, WebRequest request) {
        Map<String, Set<String>> body = ex.getConstraintViolations().stream().collect(Collectors.groupingBy(constraintViolation -> {

            final String path = constraintViolation.getPropertyPath().toString();

            if (!constraintViolation.getRootBeanClass().isAssignableFrom(Serializable.class)) {
                return path.substring(path.indexOf(".") + 1);
            } else {
                return path;
            }
        }, Collectors.mapping(ConstraintViolation::getMessage, Collectors.toSet())));

        return super.handleExceptionInternal(ex, body, null, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    protected ResponseEntity<Object> handleValidationException(ValidationException ex, HttpHeaders headers, WebRequest request) {
        return super.handleExceptionInternal(ex, List.of(ex.getMessage()), null, HttpStatus.BAD_REQUEST, request);
    }
}
