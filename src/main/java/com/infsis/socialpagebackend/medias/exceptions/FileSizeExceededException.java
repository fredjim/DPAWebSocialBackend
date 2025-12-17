package com.infsis.socialpagebackend.medias.exceptions;

public class FileSizeExceededException extends RuntimeException {
    public FileSizeExceededException(String msg) {
        super(msg);
    }
}