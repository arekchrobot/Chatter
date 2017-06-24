package pl.ark.chr.simplechat.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by arek on 24.06.17.
 */
public class RestException extends RuntimeException {

    private HttpStatus status;

    public RestException(String s, HttpStatus status) {
        super(s);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
