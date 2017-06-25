package pl.ark.chr.simplechat.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.ark.chr.simplechat.domain.Chat;

import java.io.IOException;

/**
 * Created by Arek on 2017-06-25.
 */
public class ChatSerializer extends JsonSerializer<Chat> {

    private class ChatWrapper {
        private String name;

        public ChatWrapper(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    @Override
    public void serialize(Chat chat, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeObject(new ChatWrapper(chat.getName()));
    }
}
