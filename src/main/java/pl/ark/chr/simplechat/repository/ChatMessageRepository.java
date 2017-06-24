package pl.ark.chr.simplechat.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.simplechat.domain.ChatMessage;

/**
 * Created by Arek on 2017-06-22.
 */
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
}
