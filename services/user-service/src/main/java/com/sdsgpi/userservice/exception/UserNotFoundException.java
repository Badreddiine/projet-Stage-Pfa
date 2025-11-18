package com.sdsgpi.userservice.exception;

/**
 * Exception levée quand un utilisateur n'est pas trouvé
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

