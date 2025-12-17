package com.infsis.socialpagebackend.medias.exceptions;

public class MaxFilesExceededException extends RuntimeException {
    public MaxFilesExceededException(String message) {
        super(message);
    }
}
