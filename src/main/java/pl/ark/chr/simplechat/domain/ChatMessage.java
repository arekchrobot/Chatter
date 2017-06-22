package pl.ark.chr.simplechat.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Arek on 2017-06-22.
 */
@Entity
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {

    private static final long serialVersionUID = -7161841124576597639L;

    private String content;

    private LocalDateTime created;

    private Chat chat;

    private ChatterUser sender;

    private ChatterUser receiver;

    private boolean read;

    @Column(name = "content", length = 500)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    public ChatterUser getSender() {
        return sender;
    }

    public void setSender(ChatterUser sender) {
        this.sender = sender;
    }

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    public ChatterUser getReceiver() {
        return receiver;
    }

    public void setReceiver(ChatterUser receiver) {
        this.receiver = receiver;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
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
        return new HashCodeBuilder(635, 289).append(getId()).toHashCode();
    }
}
