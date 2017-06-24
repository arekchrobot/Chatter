package pl.ark.chr.simplechat.service.impl;

import org.apache.shiro.authc.credential.PasswordService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.dto.RegisterDTO;
import pl.ark.chr.simplechat.dto.UserDTO;
import pl.ark.chr.simplechat.exceptions.RestException;
import pl.ark.chr.simplechat.repository.ChatterUserRepository;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * Created by arek on 24.06.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class RegisterServiceImplTest {

    private RegisterServiceImpl sut;

    @Mock
    private ChatterUserRepository chatterUserRepository;

    @Mock
    private PasswordService passwordService;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        sut = new RegisterServiceImpl(chatterUserRepository, passwordService);
    }

    @Test
    public void testRegister__Success() {
        //given
        String username = "Username";
        String password = "pass";

        String hash = password + "_encrypted";

        RegisterDTO register = new RegisterDTO();
        register.setConfirmPassword(password);
        register.setPassword(password);
        register.setUsername(username);

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(passwordService.encryptPassword(any(Object.class))).thenReturn(hash);

        doAnswer(invocationOnMock -> {
            ChatterUser userToSave = (ChatterUser) invocationOnMock.getArguments()[0];

            assertThat(userToSave.getPassword()).isEqualTo(hash);

            return userToSave;
        }).when(chatterUserRepository).save(any(ChatterUser.class));

        //when
        UserDTO result = sut.register(register);

        //then
        assertThat(result)
                .isNotNull();
        assertThat(result.getUsername())
                .isNotEmpty()
                .isEqualTo(username);
    }

    @Test
    public void testRegister__RegisterDTOIsNull() {
        //given

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(passwordService.encryptPassword(any(Object.class))).thenReturn("test");

        fluentThrown
                .expect(RestException.class)
                .hasMessage(RegisterServiceImpl.EMPTY_BODY);

        //when
        sut.register(null);

        //then
    }

    @Test
    public void testRegister__RegisterParamIsEmptyOrNull() {
        //given
        String username = "";
        String password = "pass";

        String hash = password + "_encrypted";

        RegisterDTO register = new RegisterDTO();
        register.setConfirmPassword(password);
        register.setPassword(password);
        register.setUsername(username);

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(passwordService.encryptPassword(any(Object.class))).thenReturn(hash);

        fluentThrown
                .expect(RestException.class)
                .hasMessage(RegisterServiceImpl.USERNAME_EMPTY);

        //when
        sut.register(register);

        //then
    }

    @Test
    public void testRegister__PasswordsNotEqual() {
        //given
        String username = "username";
        String password = "pass";

        String hash = password + "_encrypted";

        RegisterDTO register = new RegisterDTO();
        register.setConfirmPassword(password + "_wrong");
        register.setPassword(password);
        register.setUsername(username);

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(passwordService.encryptPassword(any(Object.class))).thenReturn(hash);

        fluentThrown
                .expect(RestException.class)
                .hasMessage(RegisterServiceImpl.PASSWORDS_NOT_MATCH);

        //when
        sut.register(register);

        //then
    }

    @Test
    public void testRegister__UserExists() {
        //given
        String username = "username";
        String password = "pass";

        String hash = password + "_encrypted";

        RegisterDTO register = new RegisterDTO();
        register.setConfirmPassword(password);
        register.setPassword(password);
        register.setUsername(username);

        when(chatterUserRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new ChatterUser()));
        when(passwordService.encryptPassword(any(Object.class))).thenReturn(hash);

        fluentThrown
                .expect(RestException.class)
                .hasMessage(RegisterServiceImpl.USER_EXISTS);

        //when
        sut.register(register);

        //then
    }

}