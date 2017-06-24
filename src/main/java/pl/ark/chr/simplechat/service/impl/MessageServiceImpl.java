package pl.ark.chr.simplechat.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.ark.chr.simplechat.domain.Chat;
import pl.ark.chr.simplechat.domain.ChatMessage;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.repository.ChatMessageRepository;
import pl.ark.chr.simplechat.repository.ChatRepository;
import pl.ark.chr.simplechat.repository.ChatterUserRepository;
import pl.ark.chr.simplechat.service.MessageService;
import pl.ark.chr.simplechat.util.ChatMessageBuilder;
import pl.ark.chr.simplechat.util.TokenGenerator;
import pl.ark.chr.simplechat.util.ValidationUtil;
import pl.ark.chr.simplechat.websocket.MessageEndpoint;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Arek on 2017-06-24.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    public static final String CHAT_NAME_SPLITTER = "_";
    public static final String USER_NOT_EXIST = "User not exist";
    public static final String EMPTY_CONTENT = "Content cannot be empty";
    public static final String EMPTY_RECEIVER = "Receiver cannot be empty";
    public static final String EMPTY_SENDER = "Sender cannot be empty";
    public static final String EMPTY_MSG_ID = "Message id cannot be empty";
    public static final String EMPTY_MSG = "Message not exists";
    public static final String WRONG_RECEIVER = "User is not receiver of the message";

    private ChatMessageRepository chatMessageRepository;

    private ChatRepository chatRepository;

    private ChatterUserRepository chatterUserRepository;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageServiceImpl(ChatMessageRepository chatMessageRepository, ChatRepository chatRepository,
                              ChatterUserRepository chatterUserRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRepository = chatRepository;
        this.chatterUserRepository = chatterUserRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendMessageToUser(String content, UserDTO sender, String receiver) {
        validateInput(content, sender, receiver);

        LocalDateTime timestamp = LocalDateTime.now();

        Optional<ChatterUser> receiverUser = chatterUserRepository.findByUsername(receiver);

        if (receiverUser.isPresent()) {
            Chat chat = getChat(sender, receiver);

            ChatMessage message = ChatMessageBuilder.instance()
                    .chat(chat)
                    .content(content)
                    .sender(sender.getUsername())
                    .receiver(receiver)
                    .created(timestamp)
                    .build();

            String token = TokenGenerator.generateToken(receiver);

            simpMessagingTemplate.convertAndSend(MessageEndpoint.MESSAGE_DELIVERY_URL + token, chatMessageRepository.save(message));
        } else {
            LOGGER.error("Receiver not exists. Message won't be send.");

            throw new RestException(USER_NOT_EXIST, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void markChatMessageRead(UserDTO receiver, Long messageId) {
        if (receiver == null) {
            LOGGER.error("Receiver cannot be empty. Message won't be marked as read.");

            throw new RestException(EMPTY_RECEIVER, HttpStatus.BAD_REQUEST);
        }

        ChatMessage message = getMessage(messageId);

        if (receiverDoesNotMatch(receiver, message)) {
            LOGGER.error("Wrong receiver: " + receiver.getUsername() + " for message: " + messageId);

            throw new RestException(WRONG_RECEIVER, HttpStatus.BAD_REQUEST);
        }

        message.setRead(true);

        chatMessageRepository.save(message);
    }

    private boolean receiverDoesNotMatch(UserDTO receiver, ChatMessage message) {
        return !message.getReceiver().equals(receiver.getUsername());
    }

    private ChatMessage getMessage(Long messageId) {
        if (messageId == null) {
            LOGGER.error("Message id is empty. Message won't be marked as read.");

            throw new RestException(EMPTY_MSG_ID, HttpStatus.BAD_REQUEST);
        }

        ChatMessage message = chatMessageRepository.findOne(messageId);

        if (message == null) {
            LOGGER.error("Message with id: " + messageId + " not exist.");

            throw new RestException(EMPTY_MSG, HttpStatus.BAD_REQUEST);
        }

        return message;
    }

    @Override
    public void markChatMessagesRead(UserDTO receiver, List<Long> messagesId) {
        messagesId.forEach(messageId -> markChatMessageRead(receiver, messageId));
    }

    private void validateInput(String content, UserDTO sender, String receiver) {
        if(ValidationUtil.isBlank(content)) {
            LOGGER.error("Empty content. Message won't be send.");

            throw new RestException(EMPTY_CONTENT, HttpStatus.BAD_REQUEST);
        }

        if(ValidationUtil.isBlank(receiver)) {
            LOGGER.error("Receiver is empty. Message won't be send.");

            throw new RestException(EMPTY_RECEIVER, HttpStatus.BAD_REQUEST);
        }

        if(sender == null) {
            LOGGER.error("Sender is empty. Message won't be send.");

            throw new RestException(EMPTY_SENDER, HttpStatus.BAD_REQUEST);
        }
    }

    private Chat getChat(UserDTO sender, String receiver) {
        Optional<Chat> existingChat = chatRepository.findByNameLike(sender.getUsername(), receiver);

        Chat chat;

        if (existingChat.isPresent()) {
            chat = existingChat.get();
        } else {
            chat = new Chat();
            chat.setName(sender.getUsername() + CHAT_NAME_SPLITTER + receiver);

            chat = chatRepository.save(chat);
        }

        return chat;
    }
}
