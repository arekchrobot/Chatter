package pl.ark.chr.simplechat.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Arek on 2017-06-22.
 */
@Entity
@Table(name = "chat")
public class Chat extends BaseEntity {

    private static final long serialVersionUID = -7161848974543597639L;

    private String name;

    private List<ChatMessage> messages;

    @Column(name = "name", length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chat")
    @OrderBy("created ASC")
    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
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

        final Chat ds = (Chat) object;

        return new EqualsBuilder().append(getId(), ds.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(281, 459).append(getId()).toHashCode();
    }
}
