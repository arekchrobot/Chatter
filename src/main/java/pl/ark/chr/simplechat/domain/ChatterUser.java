package pl.ark.chr.simplechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * Created by Arek on 2017-06-22.
 */
@Entity
@Table(name = "chatter_user")
public class ChatterUser extends BaseEntity {

    private static final long serialVersionUID = -2530893894458448440L;

    private String username;

    private String password;

    private Role role;

    @Column(name = "username", length = 100, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "pass", length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatter_role_id", nullable = false)
    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        final ChatterUser ds = (ChatterUser) object;

        return new EqualsBuilder().append(getId(), ds.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(263, 957).append(getId()).toHashCode();
    }
}
