package com.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleException(UserException userException) {
        return new ResponseEntity<>(userException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(BookingException bookingException) {
        return new ResponseEntity<>(bookingException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(RoomException roomException) {
        return new ResponseEntity<>(roomException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(BadCredentialsException badCredentialsException) {
        return new ResponseEntity<>(badCredentialsException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
