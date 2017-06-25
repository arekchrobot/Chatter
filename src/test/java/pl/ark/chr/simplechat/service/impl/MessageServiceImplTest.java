package pl.ark.chr.simplechat.service.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pl.ark.chr.simplechat.domain.Chat;
import pl.ark.chr.simplechat.domain.ChatMessage;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.repository.ChatMessageRepository;
import pl.ark.chr.simplechat.repository.ChatRepository;
import pl.ark.chr.simplechat.repository.ChatterUserRepository;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Created by Arek on 2017-06-24.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageServiceImplTest {

    private MessageServiceImpl sut;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatterUserRepository chatterUserRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        sut = new MessageServiceImpl(chatMessageRepository, chatRepository, chatterUserRepository, simpMessagingTemplate);
    }

    @Test
    public void testSendMessageToUser__Success_ChatNotExists() {
        //given
        String content = "Test content";
        String receiver = "receiver";
        String sender = "sender";

        ChatterUser receiverUser = new ChatterUser();
        receiverUser.setUsername(receiver);

        UserDTO currentUser = UserDTO.builder().username(sender).chats(new ArrayList<>()).build();

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.of(receiverUser));
        when(chatRepository.findByNameLike(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(chatRepository.save(any(Chat.class))).thenAnswer(invocationOnMock -> {
            Chat chat = (Chat) invocationOnMock.getArguments()[0];
            chat.setId(1L);

            return chat;
        });
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        doAnswer(invocationOnMock -> {
            ChatMessage payload = (ChatMessage) invocationOnMock.getArguments()[1];

            assertThat(payload)
                    .isNotNull();

            assertThat(payload.getChat().getName())
                    .isEqualTo(sender + MessageServiceImpl.CHAT_NAME_SPLITTER + receiver);

            assertThat(payload.getContent())
                    .isEqualTo(content);
            assertThat(payload.getSender())
                    .isEqualTo(sender);
            assertThat(payload.getReceiver())
                    .isEqualTo(receiver);
            return null;
        }).when(simpMessagingTemplate).convertAndSend(any(String.class), any(Object.class));

        //when
        sut.sendMessageToUser(content, currentUser, receiver);

        //then
    }

    @Test
    public void testSendMessageToUser__Success_ChatExists() {
        //given
        String content = "Test content";
        String receiver = "receiver";
        String sender = "sender";

        ChatterUser receiverUser = new ChatterUser();
        receiverUser.setUsername(receiver);

        UserDTO currentUser = UserDTO.builder().username(sender).build();

        String chatName = receiver + MessageServiceImpl.CHAT_NAME_SPLITTER + sender;

        Chat chat = new Chat();
        chat.setId(1L);
        chat.setName(chatName);

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.of(receiverUser));
        when(chatRepository.findByNameLike(any(String.class), any(String.class))).thenReturn(Optional.of(chat));
        when(chatRepository.save(any(Chat.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        doAnswer(invocationOnMock -> {
            ChatMessage payload = (ChatMessage) invocationOnMock.getArguments()[1];

            assertThat(payload)
                    .isNotNull();

            assertThat(payload.getChat())
                    .isEqualTo(chat);
            assertThat(payload.getChat().getName())
                    .isEqualTo(chatName);

            assertThat(payload.getContent())
                    .isEqualTo(content);
            assertThat(payload.getSender())
                    .isEqualTo(sender);
            assertThat(payload.getReceiver())
                    .isEqualTo(receiver);
            return null;
        }).when(simpMessagingTemplate).convertAndSend(any(String.class), any(Object.class));

        //when
        sut.sendMessageToUser(content, currentUser, receiver);

        //then
    }

    @Test
    public void testSendMessageToUser__ReceiverNotExists() {
        //given
        String content = "Test content";
        String receiver = "receiver";
        String sender = "sender";

        UserDTO currentUser = UserDTO.builder().username(sender).build();

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        fluentThrown
                .expect(RestException.class)
                .hasMessage(MessageServiceImpl.USER_NOT_EXIST);

        //when
        sut.sendMessageToUser(content, currentUser, receiver);

        //then
    }

    @Test
    public void testSendMessageToUser__EmptySender() {
        //given
        String content = "Test content";
        String receiver = "receiver";

        fluentThrown
                .expect(RestException.class)
                .hasMessage(MessageServiceImpl.EMPTY_SENDER);

        //when
        sut.sendMessageToUser(content, null, receiver);

        //then
    }

    @Test
    public void testSendMessageToUser__EmptyContent() {
        //given
        String receiver = "receiver";
        String sender = "sender";

        UserDTO currentUser = UserDTO.builder().username(sender).build();

        fluentThrown
                .expect(RestException.class)
                .hasMessage(MessageServiceImpl.EMPTY_CONTENT);

        //when
        sut.sendMessageToUser(null, currentUser, receiver);

        //then
    }

    @Test
    public void testSendMessageToUser__BlankReceiver() {
        //given
        String content = "content";
        String receiver = "";
        String sender = "sender";

        UserDTO currentUser = UserDTO.builder().username(sender).build();

        fluentThrown
                .expect(RestException.class)
                .hasMessage(MessageServiceImpl.EMPTY_RECEIVER);

        //when
        sut.sendMessageToUser(content, currentUser, receiver);

        //then
    }

    @Test
    public void testMarkChatMessageRead__Success() {
        //given
        String receiver = "receiver";

        UserDTO currentUser = UserDTO.builder().username(receiver).build();

        Long messageId = 1L;
        ChatMessage message = new ChatMessage();
        message.setReceiver(receiver);
        message.setId(messageId);

        when(chatMessageRepository.findOne(any(Long.class))).thenReturn(message);

        doAnswer(invocationOnMock -> {
            ChatMessage chatMessage = (ChatMessage) invocationOnMock.getArguments()[0];

            assertThat(chatMessage)
                    .isEqualTo(message);
            assertThat(chatMessage.isRead())
                    .isTrue();
            return chatMessage;
        }).when(chatMessageRepository).save(any(ChatMessage.class));

        //when
        sut.markChatMessageRead(currentUser, messageId);

        //then
    }

    @Test
    public void testMarkChatMessageRead__ReceiversDoNotMatch() {
        //given
        String receiver = "receiver";
        String messageReceiver = "msgReceiver";

        UserDTO currentUser = UserDTO.builder().username(receiver).build();

        Long messageId = 1L;
        ChatMessage message = new ChatMessage();
        message.setReceiver(messageReceiver);
        message.setId(messageId);

        when(chatMessageRepository.findOne(any(Long.class))).thenReturn(message);

        fluentThrown
                .expect(RestException.class)
                .hasMessage(MessageServiceImpl.WRONG_RECEIVER);

        //when
        sut.markChatMessageRead(currentUser, messageId);

        //then
    }

    @Test
    public void testMarkChatMessageRead__NullReceiver() {
        //given
        String receiver = "receiver";

        Long messageId = 1L;
        ChatMessage message = new ChatMessage();
        message.setReceiver(receiver);
        message.setId(messageId);

        when(chatMessageRepository.findOne(any(Long.class))).thenReturn(message);

        fluentThrown
                .expect(RestException.class)
                .hasMessage(MessageServiceImpl.EMPTY_RECEIVER);

        //when
        sut.markChatMessageRead(null, messageId);

        //then
    }

    @Test
    public void testMarkChatMessageRead__MessageNotExists() {
        //given
        String receiver = "receiver";

        UserDTO currentUser = UserDTO.builder().username(receiver).build();

        Long messageId = 1L;
        ChatMessage message = new ChatMessage();
        message.setReceiver(receiver);
        message.setId(messageId);

        when(chatMessageRepository.findOne(any(Long.class))).thenReturn(null);

        fluentThrown
                .expect(RestException.class)
                .hasMessage(MessageServiceImpl.EMPTY_MSG);

        //when
        sut.markChatMessageRead(currentUser, messageId);

        //then
    }
}