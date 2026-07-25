package com.infsis.socialpagebackend.exceptions;

/**
 * Se lanza cuando una cuenta queda temporalmente bloqueada por exceso de intentos
 * fallidos de autenticación (account lockout). Se mapea a HTTP 429 Too Many Requests.
 */
public class TooManyRequestsException extends RuntimeException {

    private final long retryAfterSeconds;

    public TooManyRequestsException(String message, long retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
