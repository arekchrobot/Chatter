package pl.ark.chr.simplechat.service.impl;

import org.apache.shiro.authc.credential.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.domain.Role;
import pl.ark.chr.simplechat.dto.RegisterDTO;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.repository.ChatterUserRepository;
import pl.ark.chr.simplechat.service.RegisterService;
import pl.ark.chr.simplechat.util.ValidationUtil;

/**
 * Created by arek on 24.06.17.
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatterUserServiceImpl.class);

    public static final String EMPTY_BODY = "Empty body";
    public static final String USERNAME_EMPTY = "Username cannot be empty";
    public static final String PASSWORD_EMPTY = "Password cannot be empty";
    public static final String PASSWORDS_NOT_MATCH = "Passwords doesn't match.";
    public static final String USER_EXISTS = "User with username already exists.";

    private ChatterUserRepository chatterUserRepository;

    private PasswordService passwordService;

    @Autowired
    public RegisterServiceImpl(ChatterUserRepository chatterUserRepository, PasswordService passwordService) {
        this.chatterUserRepository = chatterUserRepository;
        this.passwordService = passwordService;
    }

    @Override
    public UserDTO register(RegisterDTO register) {

        validateRegisterData(register);

        validateDuplicateUser(register.getUsername());

        ChatterUser user = new ChatterUser();
        user.setUsername(register.getUsername());
        user.setPassword(passwordService.encryptPassword(register.getPassword()));

        Role userRole = new Role();
        userRole.setId(Role.USER_ROLE);

        user.setRole(userRole);

        user = chatterUserRepository.save(user);

        return UserDTO.builder()
                .username(user.getUsername())
                .build();
    }

    private void validateDuplicateUser(String username) {
        chatterUserRepository.findByUsername(username)
                .ifPresent(user -> {
                    LOGGER.error("User with username: " + username + " already exists");
                    throw new RestException(USER_EXISTS, HttpStatus.BAD_REQUEST);
                });
    }

    private void validateRegisterData(RegisterDTO register) {

        if (register == null) {
            LOGGER.error("Register body is empty. Register failed.");

            throw new RestException(EMPTY_BODY, HttpStatus.BAD_REQUEST);
        }

        if (ValidationUtil.isBlank(register.getUsername())) {
            LOGGER.error("Username is empty. Register failed.");

            throw new RestException(USERNAME_EMPTY, HttpStatus.BAD_REQUEST);
        }

        if (ValidationUtil.isBlank(register.getPassword())
                || ValidationUtil.isBlank(register.getConfirmPassword())) {
            LOGGER.error("Password is empty. Register failed.");

            throw new RestException(PASSWORD_EMPTY, HttpStatus.BAD_REQUEST);
        }

        if(!register.getPassword().equals(register.getConfirmPassword())) {
            LOGGER.error("Passwords doesn't match. Registed failed.");

            throw new RestException(PASSWORDS_NOT_MATCH, HttpStatus.BAD_REQUEST);
        }
    }
}
