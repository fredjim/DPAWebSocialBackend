package com.infsis.socialpagebackend.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private int status;
    private String message;
    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    // Constructor principal con todos los campos
    public ErrorResponse(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor sin path para compatibilidad
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor para compatibilidad con código existente
    public ErrorResponse(String message) {
        this.status = 500;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Métodos para compatibilidad con código existente que usa 'exception' y 'code'
    @JsonIgnore
    public String getException() {
        return message;
    }

    public void setException(String exception) {
        this.message = exception;
    }

    @JsonIgnore
    public int getCode() {
        return status;
    }

    public void setCode(int code) {
        this.status = code;
    }

}
