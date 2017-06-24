package pl.ark.chr.simplechat.dto;

import pl.ark.chr.simplechat.domain.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arek on 24.06.17.
 */
public class UserDTO {

    private String username;

    private String socketToken;

    private List<Chat> chats = new ArrayList<>();

    public UserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSocketToken() {
        return socketToken;
    }

    public void setSocketToken(String socketToken) {
        this.socketToken = socketToken;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private UserDTO userDTO;

        UserDTOBuilder() {
            userDTO = new UserDTO();
        }

        public UserDTOBuilder username(String username) {
            this.userDTO.username = username;
            return this;
        }

        public UserDTOBuilder socketToken(String socketToken) {
            this.userDTO.socketToken = socketToken;
            return this;
        }

        public UserDTOBuilder chats(List<Chat> chats) {
            this.userDTO.chats = chats;
            return this;
        }

        public UserDTO build() {
            return userDTO;
        }
    }
}
