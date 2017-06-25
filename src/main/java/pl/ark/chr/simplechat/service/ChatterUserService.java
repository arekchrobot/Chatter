package pl.ark.chr.simplechat.service;

import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.dto.CredentialsDTO;
import pl.ark.chr.simplechat.dto.UserDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by Arek on 2017-06-22.
 */
public interface ChatterUserService {

    UserDTO login(CredentialsDTO credentials);

    Optional<ChatterUser> getByUsername(String username);

    List<UserDTO> getAll();
}
