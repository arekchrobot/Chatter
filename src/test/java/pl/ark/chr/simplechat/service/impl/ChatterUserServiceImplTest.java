package pl.ark.chr.simplechat.service.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.ark.chr.simplechat.domain.Chat;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.dto.CredentialsDTO;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.repository.ChatRepository;
import pl.ark.chr.simplechat.repository.ChatterUserRepository;
import pl.ark.chr.simplechat.websocket.service.WebSocketTokenService;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by arek on 24.06.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatterUserServiceImplTest {

    private ChatterUserServiceImpl sut;

    @Mock
    private ChatterUserRepository chatterUserRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private WebSocketTokenService webSocketTokenService;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        sut = new ChatterUserServiceImpl(chatterUserRepository, chatRepository, webSocketTokenService);
        when(webSocketTokenService.generateTokenForUser(any(ChatterUser.class))).thenReturn("TEST_TOKEN");
    }

    @Test
    public void testLogin__Success() {
        //given
        String username = "TestName";

        ChatterUser user = new ChatterUser();
        user.setUsername(username);

        List<Chat> chats = new ArrayList<>();
        Chat chat = new Chat();
        chat.setName(username + "_" + username);
        chats.add(chat);

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(chatRepository.findByNameLikeAndFetchMessagesEagerly(any(String.class))).thenReturn(chats);

        CredentialsDTO credentials = new CredentialsDTO();
        credentials.setUsername(username);
        credentials.setPassword("pass");

        //when
        UserDTO result = sut.login(credentials);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getUsername())
                .isNotEmpty()
                .isEqualTo(username);
        assertThat(result.getChats())
                .isNotNull()
                .isNotEmpty();
        assertThat(result.getSocketToken())
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    public void testLogin__CredentialsMissParam() {
        //given
        String username = "TestName";

        ChatterUser user = new ChatterUser();
        user.setUsername(username);

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        CredentialsDTO credentials = new CredentialsDTO();
        credentials.setUsername(username);

        fluentThrown
                .expect(RestException.class)
                .hasMessage(ChatterUserServiceImpl.LOGIN_ERROR_MSG);

        //when
        UserDTO result = sut.login(credentials);

        //then
    }

    @Test
    public void testLogin__UserNotFound() {
        //given
        String username = "TestName";

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        CredentialsDTO credentials = new CredentialsDTO();
        credentials.setUsername(username);
        credentials.setPassword("pass");

        fluentThrown
                .expect(RestException.class)
                .hasMessage(ChatterUserServiceImpl.LOGIN_ERROR_MSG);

        //when
        UserDTO result = sut.login(credentials);

        //then
    }

}