package pl.ark.chr.simplechat.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.simplechat.rest.annotations.POSTTextPlain;
import pl.ark.chr.simplechat.rest.annotations.RestController;
import pl.ark.chr.simplechat.service.MessageService;
import pl.ark.chr.simplechat.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2017-06-24.
 */
@RestController(BaseRestEndpoint.BASE_API_PREFIX + "/message")
public class RestMessageEndpoint extends BaseRestEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMessageEndpoint.class);

    private MessageService messageService;

    @Autowired
    public RestMessageEndpoint(SessionUtil sessionUtil, MessageService messageService) {
        super(sessionUtil);
        this.messageService = messageService;
    }

    @POSTTextPlain("/send/{receiver}")
    public boolean sendMessageToUser(HttpServletRequest request, @PathVariable("receiver") String receiver, @RequestBody String content) {
        LOGGER.info("Sending message to user: " + receiver + " from user: " + sessionUtil.getCurrentUser(request).getUsername());

        messageService.sendMessageToUser(content, sessionUtil.getCurrentUser(request), receiver);

        return true;
    }
}
