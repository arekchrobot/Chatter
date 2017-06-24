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

import java.util.Optional;

/**
 * Created by Arek on 2017-06-22.
 */
@Service
public class ChatterUserServiceImpl implements ChatterUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatterUserServiceImpl.class);

    public static final String LOGIN_ERROR_MSG = "Username or password is incorrect";

    private ChatterUserRepository chatterUserRepository;

    private ChatRepository chatRepository;


    @Autowired
    public ChatterUserServiceImpl(ChatterUserRepository chatterUserRepository, ChatRepository chatRepository) {
        this.chatterUserRepository = chatterUserRepository;
        this.chatRepository = chatRepository;
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
                .build();
    }

    @Override
    public Optional<ChatterUser> getByUsername(String username) {
        return chatterUserRepository.findByUsername(username);
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
