package dev.jacobandersen.cams.game.error.security;

public class InvalidJwtPurposeException extends Exception {
    public InvalidJwtPurposeException() {
        this(null);
    }

    public InvalidJwtPurposeException(String message) {
        super(message);
    }
}
