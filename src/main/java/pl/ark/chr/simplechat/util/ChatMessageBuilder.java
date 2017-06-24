package pl.ark.chr.simplechat.util;

import pl.ark.chr.simplechat.domain.Chat;
import pl.ark.chr.simplechat.domain.ChatMessage;

import java.time.LocalDateTime;

/**
 * Created by Arek on 2017-06-24.
 */
public class ChatMessageBuilder {

    private ChatMessage chatMessage;

    private ChatMessageBuilder() {
        this.chatMessage = new ChatMessage();
        this.chatMessage.setRead(false);
    }

    public static ChatMessageBuilder instance() {
        return new ChatMessageBuilder();
    }

    public ChatMessageBuilder content(String content) {
        this.chatMessage.setContent(content);
        return this;
    }

    public ChatMessageBuilder chat(Chat chat) {
        this.chatMessage.setChat(chat);
        return this;
    }

    public ChatMessageBuilder sender(String sender) {
        this.chatMessage.setSender(sender);
        return this;
    }

    public ChatMessageBuilder receiver(String receiver) {
        this.chatMessage.setReceiver(receiver);
        return this;
    }

    public ChatMessageBuilder created(LocalDateTime timestamp) {
        this.chatMessage.setCreated(timestamp);
        return this;
    }

    public ChatMessage build() {
        return this.chatMessage;
    }
}
