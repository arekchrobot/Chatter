package pl.ark.chr.simplechat.util;

import java.util.Base64;

/**
 * Created by Arek on 2017-06-24.
 */
public class TokenGenerator {

    private static final String TOKEN_SALT = "hreter24352dsfs34532";

    public static String generateToken(String username) {
        String rawToken = username + TOKEN_SALT;
        return Base64.getEncoder().encodeToString(
                Base64.getEncoder().encode(rawToken.getBytes()));
    }
}
