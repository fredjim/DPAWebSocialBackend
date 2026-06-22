package com.infsis.socialpagebackend.exceptions;

import com.infsis.socialpagebackend.medias.exceptions.BadRequestException;
import com.infsis.socialpagebackend.moderation.exceptions.ContentBlockedException;
import com.infsis.socialpagebackend.medias.exceptions.FileSizeExceededException;
import com.infsis.socialpagebackend.medias.exceptions.MaxFilesExceededException;
import com.infsis.socialpagebackend.medias.exceptions.UnsupportedFileTypeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest httpServletRequest) {
        ErrorResponse apiErrorResponse = new ErrorResponse(404, "Resource not found", httpServletRequest.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(apiErrorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder message = new StringBuilder("Errores de validación: ");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            message.append(fieldName).append(": ").append(errorMessage).append("; ");
        });

        ErrorResponse errorResponse = new ErrorResponse(400, message.toString().trim(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintValidationExceptions(ConstraintViolationException ex, HttpServletRequest request) {
        StringBuilder message = new StringBuilder("Errores de validación: ");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            message.append(violation.getMessage()).append("; ");
        }

        ErrorResponse errorResponse = new ErrorResponse(400, message.toString().trim(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(409, "Duplicate key error occurred. Please provide a valid value.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(400, "El archivo excede el tamaño permitido", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedDataException(IllegalArgumentException exc, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(400, exc.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(401, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ErrorResponse> handleAccountDisabled(AccountDisabledException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(403, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(java.io.IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(java.io.IOException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(500, "Error al procesar el archivo: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(com.infsis.socialpagebackend.medias.exceptions.FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(com.infsis.socialpagebackend.medias.exceptions.FileStorageException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(500, "Error de almacenamiento: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(com.infsis.socialpagebackend.medias.exceptions.InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileException(com.infsis.socialpagebackend.medias.exceptions.InvalidFileException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(400, "Archivo inválido: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(FileSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleSize(FileSizeExceededException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(build(413,"FILE_SIZE_EXCEEDED: "+ ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(UnsupportedFileTypeException.class)
    public ResponseEntity<ErrorResponse> handleType(UnsupportedFileTypeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(build(415,"UNSUPPORTED_FILE_TYPE: "+ ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MaxFilesExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxFiles(
            MaxFilesExceededException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(build(400, "MAX_FILES_EXCEEDED: "+ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(build(400, "BAD_REQUEST: "+ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ContentBlockedException.class)
    public ResponseEntity<ErrorResponse> handleContentBlocked(
            ContentBlockedException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                422,
                "Contenido bloqueado: " + ex.getModerationReason(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    private ErrorResponse build(int status, String msg, String code) {
        return new ErrorResponse(status, msg, code);
    }
}