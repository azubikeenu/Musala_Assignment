package com.azubike.ellipsis.droneapplication.aop;

import com.azubike.ellipsis.droneapplication.exception.AppException;
import com.azubike.ellipsis.droneapplication.exception.ConflictException;
import com.azubike.ellipsis.droneapplication.exception.ImageProcessingException;
import com.azubike.ellipsis.droneapplication.exception.NotFoundException;
import com.azubike.ellipsis.droneapplication.exception.response.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AppExceptionHandler {
    private final MessageSource messageSource;


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        var parsedErrors = parseErrors(ex.getBindingResult().getAllErrors());
        ErrorMessage errorMessage =
                new ErrorMessage(
                        new Date(), parsedErrors.toString(),path);


        return new ResponseEntity<>( errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundHandler(NotFoundException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), ex.getMessage(), path);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> conflictExceptionHandler(ConflictException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        ErrorMessage errorMessage =
                new ErrorMessage(new Date(), ex.getMessage(), path);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }



    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();

        ErrorMessage errorMessage =
                new ErrorMessage(
                        new Date(), ex.getMessage(), path);
        ex.printStackTrace();
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<Object> handleAppException(AppException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        ErrorMessage errorMessage =
                new ErrorMessage(
                        new Date(), ex.getMessage(), path);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ImageProcessingException.class})
    public ResponseEntity<Object> handleImageException(ImageProcessingException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        ErrorMessage errorMessage =
                new ErrorMessage(
                        new Date(), ex.getMessage(), path);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private List<String> parseErrors(List<ObjectError> allErrors) {
        return allErrors.stream().map(this::localizeErrorMessage).collect(Collectors.toList());
    }


    private String localizeErrorMessage(ObjectError objectError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(objectError, currentLocale);
        log.info(localizedErrorMessage);
        return localizedErrorMessage;
    }
}
