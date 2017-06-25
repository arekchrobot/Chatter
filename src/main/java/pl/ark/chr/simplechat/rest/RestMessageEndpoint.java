package pl.ark.chr.simplechat.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.simplechat.domain.ChatMessage;
import pl.ark.chr.simplechat.rest.annotations.POSTTextPlain;
import pl.ark.chr.simplechat.rest.annotations.PUT;
import pl.ark.chr.simplechat.rest.annotations.RestController;
import pl.ark.chr.simplechat.service.MessageService;
import pl.ark.chr.simplechat.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
    public ChatMessage sendMessageToUser(HttpServletRequest request, @PathVariable("receiver") String receiver, @RequestBody String content) {
        LOGGER.info("Sending message to user: " + receiver + " from user: " + sessionUtil.getCurrentUser(request).getUsername());

        return messageService.sendMessageToUser(content, sessionUtil.getCurrentUser(request), receiver);
    }

    @PUT("/markRead/{messageId}")
    public boolean markMessageRead(HttpServletRequest request, @PathVariable("messageId") Long messageId) {
        LOGGER.info("Marking message with id: " + messageId + " for user: " + sessionUtil.getCurrentUser(request).getUsername());

        messageService.markChatMessageRead(sessionUtil.getCurrentUser(request), messageId);

        return true;
    }

    @PUT("/markAllRead/{messagesId}")
    public boolean markMessagesRead(HttpServletRequest request, @PathVariable("messageId") List<Long> messagesId) {
        LOGGER.info("Marking messages with ids: " + messagesId.stream().map(Object::toString).collect(Collectors.joining(","))
                + " for user: " + sessionUtil.getCurrentUser(request).getUsername());

        messageService.markChatMessagesRead(sessionUtil.getCurrentUser(request), messagesId);

        return true;
    }
}
