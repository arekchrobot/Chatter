package pl.ark.chr.simplechat.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2017-06-22.
 */
public class HttpUtil {

    public static String generateOriginalUrl(HttpServletRequest request) {
        return request.getRequestURL().append("?")
                .append(request.getQueryString() != null ? request.getQueryString() : "").toString();
    }
}
