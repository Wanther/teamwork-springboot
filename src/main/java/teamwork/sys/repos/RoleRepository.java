package teamwork.sys.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.sys.models.Role;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
    @EntityGraph(attributePaths = "resources")
    List<Role> findAll();
}
