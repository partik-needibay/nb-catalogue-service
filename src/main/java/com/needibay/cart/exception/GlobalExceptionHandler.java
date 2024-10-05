package com.needibay.cart.exception;

import com.needibay.cart.exception.Coupon.CouponNotApplicableException;
import com.needibay.cart.response.Response;
import com.needibay.cart.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private EmailUtil emailUtil;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        Response resp = new Response.Build()
                .setSuccess(false)
                .setError(errors)
                .build();
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CouponNotApplicableException.class})
    public ResponseEntity<Object> handleGstExistException(CouponNotApplicableException exception) {

        Response resp = new Response.Build()
                .setSuccess(false)
                .setMessage(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {

        // Check if it's a NullPointerException
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Send an error email for all other exceptions
//        emailUtil.sendErrorEmail("Error in GlobalExceptionHandler", ex);

        Response resp = new Response.Build()
                .setSuccess(false)
                .setMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
//        emailUtil.sendErrorEmail("Error in Database", ex);
        Response resp = new Response.Build()
                .setSuccess(false)
                .setMessage("Database error: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PartialContentException.class)
    public ResponseEntity<Object> handlePartialContentException(PartialContentException ex) {
        Response resp = new Response.Build()
                .setSuccess(false)
                .setMessage("Partial content error: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(resp, HttpStatus.PARTIAL_CONTENT);
    }


}