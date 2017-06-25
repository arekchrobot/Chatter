package pl.ark.chr.simplechat.exceptions;

/**
 * Created by Arek on 2017-06-25.
 */
public class TokenAlreadyInUseException extends RuntimeException {

    public TokenAlreadyInUseException(String message) {
        super(message);
    }

    public TokenAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
