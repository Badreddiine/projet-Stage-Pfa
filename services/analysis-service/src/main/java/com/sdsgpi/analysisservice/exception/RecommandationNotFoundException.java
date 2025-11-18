package com.sdsgpi.analysisservice.exception;

/**
 * Exception levée quand une recommandation n'est pas trouvée
 */
public class RecommandationNotFoundException extends RuntimeException {
    
    public RecommandationNotFoundException(String message) {
        super(message);
    }
    
    public RecommandationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

