package pl.ark.chr.simplechat.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.rest.annotations.GET;
import pl.ark.chr.simplechat.rest.annotations.RestController;
import pl.ark.chr.simplechat.service.ChatterUserService;
import pl.ark.chr.simplechat.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Arek on 2017-06-25.
 */
@RestController(BaseRestEndpoint.BASE_API_PREFIX + "/user")
public class UserEndpoint extends BaseRestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    private ChatterUserService chatterUserService;

    @Autowired
    public UserEndpoint(SessionUtil sessionUtil, ChatterUserService chatterUserService) {
        super(sessionUtil);
        this.chatterUserService = chatterUserService;
    }

    @GET("/")
    public List<UserDTO> getAllUsers(HttpServletRequest request) {
        LOGGER.info("Getting all users for: " + sessionUtil.getCurrentUser(request).getUsername());

        return chatterUserService.getAll();
    }
}
