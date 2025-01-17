package com.app.mifi.exception;

import com.app.mifi.response.MiFiError;
import com.app.mifi.response.MiFiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class MiFiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MiFiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation Exception occurred",ex);
       List<MiFiError> errors =new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            MiFiError miFiError = MiFiError.builder().build();
            miFiError.setErrorCause(error.getField());
        miFiError.setErrorMessage(error.getDefaultMessage());
        errors.add(miFiError);
         });
        MiFiResponse miFiResponse = MiFiResponse.builder().errors(errors).build();
        return new ResponseEntity<>(miFiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MiFiResponse> handleGenericException(Exception ex) {
        log.error("Unknown Exception occurred",ex);
        MiFiResponse miFiResponse = MiFiResponse.builder().errors(Collections.singletonList(MiFiError.builder().errorCause("Something went wrong").errorMessage(ex.getMessage()).build())).build();
        return new ResponseEntity<>(miFiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<MiFiResponse> handleIOException(IOException ex) {
        log.error("Unknown Exception occurred",ex);
        MiFiResponse miFiResponse = MiFiResponse.builder().errors(Collections.singletonList(MiFiError.builder().errorCause("Something went wrong").errorMessage(ex.getMessage()).build())).build();
        return new ResponseEntity<>(miFiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MiFiException.class)
    public ResponseEntity<MiFiResponse> handleMifiException(MiFiException ex) {
        log.error("Mifi Exception occurred, [{}],[{}]",ex.getErrorType(),ex.getErrorMessage());
        MiFiResponse miFiResponse = MiFiResponse.builder().errors(Collections.singletonList(MiFiError.builder().errorType(ex.getErrorType()).errorMessage(ex.getErrorMessage()).build())).build();
        return new ResponseEntity<>(miFiResponse,ex.getStatus());
    }
}
