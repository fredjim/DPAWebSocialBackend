package com.infsis.socialpagebackend.security;

/**
 * Se lanza cuando un token cifrado no se puede descifrar, típicamente porque
 * la clave {@code security.facebook.token-secret} actual no coincide con la
 * que se usó para cifrarlo (BadPaddingException). Permite a los servicios
 * distinguir este caso recuperable de otros errores inesperados.
 */
public class TokenDecryptionException extends RuntimeException {
    public TokenDecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
