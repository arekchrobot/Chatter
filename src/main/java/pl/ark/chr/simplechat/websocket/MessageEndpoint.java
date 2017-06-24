package pl.ark.chr.simplechat.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import pl.ark.chr.simplechat.domain.ChatMessage;
import pl.ark.chr.simplechat.websocket.service.WebSocketTokenService;

/**
 * Created by Arek on 2017-06-24.
 */
@Controller
public class MessageEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEndpoint.class);

    public static final String MESSAGE_DELIVERY_URL = "/topic/message/receive";

    private WebSocketTokenService webSocketTokenService;

    @Autowired
    public MessageEndpoint(WebSocketTokenService webSocketTokenService) {
        this.webSocketTokenService = webSocketTokenService;
    }

    @SendTo(MESSAGE_DELIVERY_URL + "{token}")
    public ChatMessage sendMessage(@DestinationVariable String token, ChatMessage message) {
        if (webSocketTokenService.checkTokenValidity(token)) {

            return message;
        } else {
            LOGGER.info("Token: " + token +" is not active. No message will be sent.");
            return null;
        }
    }
}
