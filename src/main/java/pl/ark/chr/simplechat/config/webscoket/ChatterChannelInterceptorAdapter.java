package pl.ark.chr.simplechat.config.webscoket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import pl.ark.chr.simplechat.exceptions.TokenAlreadyInUseException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arek on 2017-06-25.
 */
public class ChatterChannelInterceptorAdapter extends ChannelInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ChatterChannelInterceptorAdapter.class);

    private static final String DESTINATION_SPLITTER = "/";

    private static volatile Map<String, String> sessionTokens = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String[] destinations = headerAccessor.getDestination().split(DESTINATION_SPLITTER);
            String token = destinations[destinations.length - 1];

            if (sessionTokens.containsValue(token)) {
                throw new TokenAlreadyInUseException("Token: " + token + " is already used");
            } else {
                logger.info("Activating websocket token: " + token);
                sessionTokens.put(headerAccessor.getSessionId(), token);
            }
        } else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
            if (sessionTokens.containsKey(headerAccessor.getSessionId())) {
                String token = sessionTokens.get(headerAccessor.getSessionId());
                logger.info("Deactivating websocket token: " + token);
                sessionTokens.remove(headerAccessor.getSessionId());
            }
        }

        return super.preSend(message, channel);
    }
}
