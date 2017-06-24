package pl.ark.chr.simplechat.rest;

import pl.ark.chr.simplechat.util.SessionUtil;

/**
 * Created by Arek on 2017-06-22.
 */
public abstract class BaseRestEndpoint {

    public static final String BASE_API_PREFIX = "/api";

    protected SessionUtil sessionUtil;

    public BaseRestEndpoint(SessionUtil sessionUtil) {
        this.sessionUtil = sessionUtil;
    }
}
