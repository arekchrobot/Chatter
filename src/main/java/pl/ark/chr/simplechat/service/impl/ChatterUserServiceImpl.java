package pl.ark.chr.simplechat.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.dto.CredentialsDTO;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.repository.ChatRepository;
import pl.ark.chr.simplechat.repository.ChatterUserRepository;
import pl.ark.chr.simplechat.service.ChatterUserService;
import pl.ark.chr.simplechat.util.ValidationUtil;
import pl.ark.chr.simplechat.websocket.service.WebSocketTokenService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-06-22.
 */
@Service
public class ChatterUserServiceImpl implements ChatterUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatterUserServiceImpl.class);

    public static final String LOGIN_ERROR_MSG = "Username or password is incorrect";

    private ChatterUserRepository chatterUserRepository;

    private ChatRepository chatRepository;

    private WebSocketTokenService webSocketTokenService;

    @Autowired
    public ChatterUserServiceImpl(ChatterUserRepository chatterUserRepository, ChatRepository chatRepository,
                                  WebSocketTokenService webSocketTokenService) {
        this.chatterUserRepository = chatterUserRepository;
        this.chatRepository = chatRepository;
        this.webSocketTokenService = webSocketTokenService;
    }

    @Override
    public UserDTO login(CredentialsDTO credentials) {

        validateCredentials(credentials);

        Optional<ChatterUser> chatterUser = chatterUserRepository.findByUsername(credentials.getUsername());

        if (!chatterUser.isPresent()) {
            LOGGER.error("No user found with username: " + credentials.getUsername());

            throw new RestException(LOGIN_ERROR_MSG, HttpStatus.BAD_REQUEST);
        }

        ChatterUser user = chatterUser.get();

        return UserDTO.builder()
                .username(user.getUsername())
                .chats(chatRepository.findByNameLikeAndFetchMessagesEagerly(user.getUsername()))
                .socketToken(webSocketTokenService.generateTokenForUser(user))
                .build();
    }

    @Override
    public Optional<ChatterUser> getByUsername(String username) {
        return chatterUserRepository.findByUsername(username);
    }

    @Override
    public List<UserDTO> getAll() {
        return ((List<ChatterUser>)chatterUserRepository.findAll()).stream()
                .map(user -> UserDTO.builder()
                        .username(user.getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void updateChatsForUser(UserDTO user) {
        if (user != null) {
            user.setChats(chatRepository.findByNameLikeAndFetchMessagesEagerly(user.getUsername()));
        }
    }

    private void validateCredentials(CredentialsDTO credentials) {
        if (credentials == null) {
            LOGGER.error("Credentials are null. Login failed.");

            throw new RestException(LOGIN_ERROR_MSG, HttpStatus.BAD_REQUEST);
        }

        if(ValidationUtil.isBlank(credentials.getUsername())) {
            LOGGER.error("Username is empty. Login failed.");

            throw new RestException(LOGIN_ERROR_MSG, HttpStatus.BAD_REQUEST);
        }

        if(ValidationUtil.isBlank(credentials.getPassword())) {
            LOGGER.error("Password is empty. Login failed.");

            throw new RestException(LOGIN_ERROR_MSG, HttpStatus.BAD_REQUEST);
        }
    }
}
