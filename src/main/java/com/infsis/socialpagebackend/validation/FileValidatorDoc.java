package com.infsis.socialpagebackend.validation;

import com.infsis.socialpagebackend.medias.exceptions.FileSizeExceededException;
import com.infsis.socialpagebackend.medias.exceptions.MaxFilesExceededException;
import com.infsis.socialpagebackend.medias.exceptions.UnsupportedFileTypeException;
import com.infsis.socialpagebackend.medias.exceptions.BadRequestException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Component
public class FileValidatorDoc {

    private static final int MAX_FILES = 5;
    private static final long MAX_SIZE = 10 * 1024 * 1024; // 10MB

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg"
    );

    public void validateSingle(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new FileSizeExceededException("Máximo permitido: 5MB");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new UnsupportedFileTypeException("Tipo no permitido");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new UnsupportedFileTypeException("Solo se permiten PDFs");
        }
    }

    public void validate(List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw new BadRequestException("No se enviaron archivos");
        }

        if (files.size() > MAX_FILES) {
            throw new MaxFilesExceededException("Máximo 5 archivos");
        }

        for (MultipartFile file : files) {
            validateSingle(file);
        }
    }
}