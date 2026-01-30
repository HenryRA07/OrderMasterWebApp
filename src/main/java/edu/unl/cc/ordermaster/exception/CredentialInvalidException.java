package edu.unl.cc.ordermaster.exception;

public class CredentialInvalidException extends Exception {
    public CredentialInvalidException() {
        super("Credenciales Invalidas");
    }

    public CredentialInvalidException(String message) {
        super(message);
    }

    public CredentialInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}

