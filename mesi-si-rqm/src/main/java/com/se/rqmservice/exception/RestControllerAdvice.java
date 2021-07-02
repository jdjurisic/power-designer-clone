package com.se.rqmservice.exception;

import com.se.rqmservice.model.DDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NasException.class)
    public ResponseEntity<?> handleNasException(NasException nasException){
        return new ResponseEntity<>(nasException.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException illegalStateException){
        return new ResponseEntity<>(illegalStateException.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<DDocument> handleConflictException(ConflictException conflictException){
        return new ResponseEntity<>(conflictException.document(), HttpStatus.CONFLICT);
    }

//
//    @ExceptionHandler(value= {
//            IllegalArgumentException.class,
//            IllegalStateException.class })
//
//    @ExceptionHandler(ConflictException.class)
//    protected ResponseEntity<Object> handleConflict(ConflictException ex, WebRequest request) {
//        return handleExceptionInternal(ex, ex.document(), new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
}
