package com.sdsgpi.userservice.exception;

/**
 * Exception levée quand on tente de créer un utilisateur qui existe déjà
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

