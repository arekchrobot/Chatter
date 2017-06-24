package pl.ark.chr.simplechat.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Arek on 2017-06-22.
 */
@Entity
@Table(name = "chatter_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = -160920230822990299L;

    public static Long USER_ROLE = 1L;

    private String name;

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        final Role ds = (Role) object;

        return new EqualsBuilder().append(getId(), ds.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(563, 757).append(getId()).toHashCode();
    }
}
