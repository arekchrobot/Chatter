package pl.ark.chr.simplechat.rest;

import pl.ark.chr.simplechat.rest.annotations.RestController;
import pl.ark.chr.simplechat.util.SessionUtil;

/**
 * Created by Arek on 2017-06-22.
 */
@RestController("/api")
public abstract class BaseRestEndpoint {

    protected SessionUtil sessionUtil;

    public BaseRestEndpoint(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }
}
