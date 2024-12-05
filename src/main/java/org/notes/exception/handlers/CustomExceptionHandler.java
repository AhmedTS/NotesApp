package org.notes.exception.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.notes.exception.exceptions.NoteNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorMessage error = new ErrorMessage("Validation Failed", details);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex){
        List<String> details = new ArrayList<>();
        for (ConstraintViolation<?> error : ex.getConstraintViolations()) {
            details.add(error.getMessage());
        }
        ErrorMessage error = new ErrorMessage("Validation Failed", details);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(NoteNotFoundException ex) {
        List<String> details = List.of("No note matching id "+ex.getNoteId());
        ErrorMessage error = new ErrorMessage("Note not found", details);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

