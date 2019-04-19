package teamwork.sys.auth.role;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, String>, JpaSpecificationExecutor<Role> {
    @EntityGraph(attributePaths = "resources")
    List<Role> findAll();

    @Modifying
    @Query("delete from Role where id in (?1)")
    void deleteAllById(Iterable<String> ids);
}
