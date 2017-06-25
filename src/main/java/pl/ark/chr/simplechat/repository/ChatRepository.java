package pl.ark.chr.simplechat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.simplechat.domain.Chat;

import java.util.List;
import java.util.Optional;

/**
 * Created by Arek on 2017-06-22.
 */
public interface ChatRepository extends CrudRepository<Chat, Long> {

    @Query("SELECT DISTINCT c FROM Chat c LEFT JOIN FETCH c.messages WHERE c.name LIKE CONCAT('%', ?1, '%')")
    List<Chat> findByNameLikeAndFetchMessagesEagerly(String sender);

    @Query("SELECT c FROM Chat c WHERE c.name LIKE CONCAT('%', ?1, '%') AND c.name LIKE CONCAT('%', ?2, '%')")
    Optional<Chat> findByNameLike(String sender, String receiver);
}
