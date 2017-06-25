package pl.ark.chr.simplechat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.util.ExceptionWrapper;
import pl.ark.chr.simplechat.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-06-22.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ExceptionWrapper> runtimeErrorHandler(HttpServletRequest request, Locale locale, RuntimeException e) {
        logRequest(request, e);

        ExceptionWrapper error;

        if (e instanceof RestException) {
            RestException ex = (RestException) e;
            error = new ExceptionWrapper(ex.getStatus().value(), ex.getMessage());
        } else {
            error = new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(error, headers, HttpStatus.valueOf(error.getStatus()));
    }

    private void logRequest(HttpServletRequest request, RuntimeException e) {
            String loggerMsg = new StringBuffer(70)
                    .append("Error executing url: ")
                    .append(HttpUtil.generateOriginalUrl(request))
                    .append(". With exception: ")
                    .append(e.toString())
                    .toString();
            LOGGER.info(loggerMsg, e);
    }
}
