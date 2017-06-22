package pl.ark.chr.simplechat.util;

import org.springframework.stereotype.Component;
import pl.ark.chr.simplechat.domain.ChatterUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2017-06-22.
 */
@Component
public class SessionUtil {

    private static final String USER_KEY = "chatter_user";

    public ChatterUser getCurrentUser(HttpServletRequest request) {
        return (ChatterUser) request.getSession().getAttribute(USER_KEY);
    }

    public void setCurrentUser(HttpServletRequest request, ChatterUser currentUser) {
        request.getSession().setAttribute(USER_KEY, currentUser);
    }

    public void removeCurrentUser(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_KEY);
    }
}
