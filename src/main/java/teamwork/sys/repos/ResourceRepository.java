package teamwork.sys.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.sys.models.Resource;

import java.util.List;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, String> {

    @EntityGraph(attributePaths = {"roles"})
    List<Resource> findAll();

}
