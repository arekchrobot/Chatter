package pl.ark.chr.simplechat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Arek on 2017-06-22.
 */
@Component
@ConfigurationProperties("chatter")
public class ChatterProperties {

    private int bcryptStrength;

    public int getBcryptStrength() {
        return bcryptStrength;
    }

    public void setBcryptStrength(int bcryptStrength) {
        this.bcryptStrength = bcryptStrength;
    }
}
