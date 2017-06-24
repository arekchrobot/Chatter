package pl.ark.chr.simplechat.websocket.service;

import pl.ark.chr.simplechat.domain.ChatterUser;

/**
 * Created by Arek on 2017-06-24.
 */
public interface WebSocketTokenService {

    boolean checkTokenValidity(String token);

    String generateTokenForUser(ChatterUser chatterUser);

    void removeToken(String token);
}
