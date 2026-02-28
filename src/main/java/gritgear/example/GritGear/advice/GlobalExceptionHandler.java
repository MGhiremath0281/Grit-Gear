package gritgear.example.GritGear.advice;

import gritgear.example.GritGear.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException; // or javax.validation

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // -------------------- VALIDATION ERRORS --------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        StringBuilder details = new StringBuilder();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> details.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, details.toString(), request);
    }

    // -------------------- CONSTRAINT VIOLATION (QUERY / PATH PARAM) --------------------
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        StringBuilder details = new StringBuilder();
        ex.getConstraintViolations()
                .forEach(violation -> details.append(violation.getPropertyPath())
                        .append(": ")
                        .append(violation.getMessage())
                        .append("; "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, details.toString(), request);
    }

    // -------------------- MALFORMED JSON --------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String detail = "Request body is malformed or contains invalid data types.";

        if (ex.getMostSpecificCause() != null &&
                ex.getMostSpecificCause().getMessage() != null &&
                ex.getMostSpecificCause().getMessage().contains("Cannot deserialize")) {

            detail = "Invalid data type provided. Please check field values.";
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, detail, request);
    }

    // -------------------- MISSING QUERY PARAM --------------------
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String message = String.format(
                "Required query parameter '%s' is missing.",
                ex.getParameterName()
        );

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    // -------------------- NOT FOUND EXCEPTIONS --------------------
    @ExceptionHandler({
            UserNotFoundException.class,
            RetailernotFoundException.class,
            CartNotFoundException.class,
            CartitemNotFoundException.class,
            OrderNotFoundException.class,
            CategorynotFoundException.class,
            OderItemNotFoundException.class,
            ProductNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // -------------------- COMMON BUILDER METHOD --------------------
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }
}