package com.infsis.socialpagebackend.validation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileValidator.class})
public @interface ValidDocumentFile {
    Class<? extends Payload> [] payload() default{};
    Class<?>[] groups() default {};
    String message() default "Only pdf files are allowed";
}
//validar si no hay archivos cargados
//validar si el archivo es nulo o esta vacio
//validar el tipo de contenido del archivo (solo pdf)
//retornar true si todas las validaciones pasan
//retornar false si alguna validacion falla
//usar esta anotacion en los campos de tipo MultipartFile que deban ser validados como archivos pdf
