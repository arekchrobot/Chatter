package pl.ark.chr.simplechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.ark.chr.simplechat.serializer.ChatSerializer;

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

    private String sender;

    private String receiver;

    private boolean read;

    @Column(name = "content", length = 500)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "created")
    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonSerialize(using = ChatSerializer.class)
    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
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

        final ChatMessage ds = (ChatMessage) object;

        return new EqualsBuilder().append(getId(), ds.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(635, 289).append(getId()).toHashCode();
    }
}
