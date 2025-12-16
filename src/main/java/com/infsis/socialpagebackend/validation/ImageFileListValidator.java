package com.infsis.socialpagebackend.validation;

import com.infsis.socialpagebackend.constants.ImageContentTypes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageFileListValidator implements ConstraintValidator<ValidImageFile, List<MultipartFile>> {

    @Override
    public void initialize(ValidImageFile constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files == null || files.isEmpty()) {
            return false;
        }
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                return false;
            }
            if (!ImageContentTypes.isSupported(file.getContentType())) {
                return false;
            }
        }
        return true;
    }
}