package pl.ark.chr.simplechat.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.simplechat.dto.CredentialsDTO;
import pl.ark.chr.simplechat.dto.RegisterDTO;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.rest.annotations.GET;
import pl.ark.chr.simplechat.rest.annotations.POST;
import pl.ark.chr.simplechat.rest.annotations.RestController;
import pl.ark.chr.simplechat.service.ChatterUserService;
import pl.ark.chr.simplechat.service.RegisterService;
import pl.ark.chr.simplechat.service.impl.ChatterUserServiceImpl;
import pl.ark.chr.simplechat.util.SessionUtil;
import pl.ark.chr.simplechat.util.TokenGenerator;
import pl.ark.chr.simplechat.websocket.service.WebSocketTokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by Arek on 2017-06-22.
 */
@RestController(BaseRestEndpoint.BASE_API_PREFIX + "/auth")
public class AuthEndpoint extends BaseRestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEndpoint.class);

    private ChatterUserService chatterUserService;

    private RegisterService registerService;

    private WebSocketTokenService webSocketTokenService;

    @Autowired
    public AuthEndpoint(SessionUtil sessionUtil, ChatterUserService chatterUserService, RegisterService registerService,
                        WebSocketTokenService webSocketTokenService) {
        super(sessionUtil);
        this.chatterUserService = chatterUserService;
        this.registerService = registerService;
    }

    @POST("/login")
    public UserDTO login(HttpServletRequest request, @RequestBody CredentialsDTO credentials) {
        UsernamePasswordToken authToken = new UsernamePasswordToken(credentials.getUsername().toLowerCase(), credentials.getPassword(), true);

        LOGGER.info("Logging user: " + credentials.getUsername());

        try {
            SecurityUtils.getSubject().login(authToken);

            UserDTO loggedUser = chatterUserService.login(credentials);
            sessionUtil.setCurrentUser(request, loggedUser);

            return loggedUser;
        } catch (AuthenticationException exception) {
            LOGGER.info("Not user found with username: " + credentials.getUsername() + " and password: " + credentials.getPassword());
            throw new RestException(ChatterUserServiceImpl.LOGIN_ERROR_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @POST("/signout")
    public void logout(HttpServletRequest request) {
        UserDTO currentUser = sessionUtil.getCurrentUser(request);
        String username = currentUser != null ? currentUser.getUsername() : "";
        LOGGER.info("Logging out user: " + username);

        SecurityUtils.getSubject().logout();

        String token = TokenGenerator.generateToken(username);

        webSocketTokenService.removeToken(token);
        sessionUtil.removeCurrentUser(request);
    }

    @GET("/logged")
    public UserDTO isLogged(HttpServletRequest request) {
        return sessionUtil.getCurrentUser(request);
    }

    @POST("/register")
    public UserDTO register(HttpServletRequest request, @RequestBody RegisterDTO register) {
        LOGGER.info("Registering user with username: " + register.getUsername());

        return registerService.register(register);
    }
}
