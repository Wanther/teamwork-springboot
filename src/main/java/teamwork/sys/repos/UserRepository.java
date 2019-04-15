package teamwork.sys.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.sys.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {

    //@Cacheable("c1")
    @EntityGraph(attributePaths = "roles")
    Optional<User> findRolesByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findRolesById(Long id);

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("delete from User where id in (?1)")
    void deleteByIds(Long[] ids);
}
