package com.infsis.socialpagebackend.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Constantes para tipos de contenido de imágenes soportados por la aplicación
 */
public final class ImageContentTypes {

    private ImageContentTypes() {
        // Constructor privado para evitar instanciación
    }

    public static final String PNG = "image/png";
    public static final String JPEG = "image/jpeg";
    public static final String JPG = "image/jpg";
    public static final String WEBP = "image/webp";

    /**
     * Lista de todos los tipos de contenido de imagen soportados
     */
    public static final List<String> SUPPORTED_TYPES = Arrays.asList(
        PNG, JPEG, JPG, WEBP
    );

    /**
     * Verifica si un tipo de contenido es soportado
     * @param contentType el tipo de contenido a verificar
     * @return true si es soportado, false en caso contrario
     */
    public static boolean isSupported(String contentType) {
        return contentType != null && SUPPORTED_TYPES.contains(contentType.toLowerCase());
    }
}
