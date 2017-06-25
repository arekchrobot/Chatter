package pl.ark.chr.simplechat.service;

import pl.ark.chr.simplechat.domain.ChatMessage;
import pl.ark.chr.simplechat.dto.UserDTO;

import java.util.List;

/**
 * Created by Arek on 2017-06-24.
 */
public interface MessageService {

    ChatMessage sendMessageToUser(String content, UserDTO sender, String receiver);

    void markChatMessageRead(UserDTO receiver, Long messageId);

    void markChatMessagesRead(UserDTO receiver, List<Long> messagesId);
}
