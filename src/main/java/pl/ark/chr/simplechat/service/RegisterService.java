package pl.ark.chr.simplechat.service;

import pl.ark.chr.simplechat.dto.RegisterDTO;
import pl.ark.chr.simplechat.dto.UserDTO;

/**
 * Created by arek on 24.06.17.
 */
public interface RegisterService {

    UserDTO register(RegisterDTO register);
}
