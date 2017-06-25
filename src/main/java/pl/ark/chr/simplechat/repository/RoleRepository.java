package pl.ark.chr.simplechat.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.simplechat.domain.Role;

/**
 * Created by Arek on 2017-06-25.
 */
public interface RoleRepository extends CrudRepository<Role, Long>{
}
