package teamwork.sys.auth.resource;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ResourceRepository extends CrudRepository<Resource, String>, JpaSpecificationExecutor<Resource> {

    @EntityGraph(attributePaths = {"roles"})
    List<Resource> findAll();

}
