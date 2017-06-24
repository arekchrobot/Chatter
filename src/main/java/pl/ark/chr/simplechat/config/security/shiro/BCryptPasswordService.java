package pl.ark.chr.simplechat.config.security.shiro;

import org.apache.shiro.authc.credential.PasswordService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ark.chr.simplechat.ChatterProperties;

/**
 * Created by Arek on 2017-06-22.
 */
@Component
public class BCryptPasswordService implements PasswordService {

    private ChatterProperties chatterProperties;

    @Autowired
    public BCryptPasswordService(ChatterProperties chatterProperties) {
        this.chatterProperties = chatterProperties;
    }

    @Override
    public String encryptPassword(Object plaintextPassword) throws IllegalArgumentException {
        final String str;
        if (plaintextPassword instanceof char[]) {
            str = new String((char[]) plaintextPassword);
        } else if (plaintextPassword instanceof String) {
            str = (String) plaintextPassword;
        } else {
            throw new SecurityException("Unsupported password type: " + plaintextPassword.getClass().getName());
        }
        return BCrypt.hashpw(str, BCrypt.gensalt(chatterProperties.getBcryptStrength()));
    }

    @Override
    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
        return BCrypt.checkpw(new String((char[]) submittedPlaintext), encrypted);
    }
}
