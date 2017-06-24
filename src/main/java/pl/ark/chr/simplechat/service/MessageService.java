package pl.ark.chr.simplechat.service;

import pl.ark.chr.simplechat.dto.UserDTO;

/**
 * Created by Arek on 2017-06-24.
 */
public interface MessageService {

    void sendMessageToUser(String content, UserDTO sender, String receiver);
}
