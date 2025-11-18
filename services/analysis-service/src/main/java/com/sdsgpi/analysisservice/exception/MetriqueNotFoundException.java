package com.sdsgpi.analysisservice.exception;

/**
 * Exception levée quand une métrique n'est pas trouvée
 */
public class MetriqueNotFoundException extends RuntimeException {
    
    public MetriqueNotFoundException(String message) {
        super(message);
    }
    
    public MetriqueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

