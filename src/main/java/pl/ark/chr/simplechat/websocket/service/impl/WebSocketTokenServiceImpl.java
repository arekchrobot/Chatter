package pl.ark.chr.simplechat.websocket.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.util.TokenGenerator;
import pl.ark.chr.simplechat.websocket.service.WebSocketTokenService;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Arek on 2017-06-24.
 */
@Service
public class WebSocketTokenServiceImpl implements WebSocketTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketTokenServiceImpl.class);

    private static volatile Set<String> tokens = new HashSet<>();

    private static final ReadWriteLock tokenLock = new ReentrantReadWriteLock(true);
    private static final Lock tokenReadLock = tokenLock.readLock();
    private static final Lock tokenWriteLock = tokenLock.writeLock();


    @Override
    public boolean checkTokenValidity(String token) {
        boolean isTokenValid = false;

        try {
            tokenReadLock.lock();

            isTokenValid = tokens.contains(token);
        } finally {
            tokenReadLock.unlock();
        }

        return isTokenValid;
    }

    @Override
    public String generateTokenForUser(ChatterUser chatterUser) {
        String token = TokenGenerator.generateToken(chatterUser.getUsername());

        try {
            tokenWriteLock.lock();
            if (!tokens.contains(token)) {
                LOGGER.info("Creating websocket token for user: " + chatterUser.getUsername() + " with value: " + token);
                tokens.add(token);
            }
        } finally {
            tokenWriteLock.unlock();
        }

        return token;
    }

    @Override
    public void removeToken(String token) {
        try {
            tokenWriteLock.lock();

            LOGGER.info("Removing token from active tokens: " + token);

            tokens.remove(token);
        } finally {
            tokenWriteLock.unlock();
        }
    }
}
