package teamwork.sys.repos;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.sys.models.Lookup;

import java.util.List;
import java.util.Optional;

@Repository
public interface LookupRepository extends CrudRepository<Lookup, Long> {

    List<Lookup> findByType(String type);

    Optional<Lookup> findByTypeAndValue(String type, String value);

}
