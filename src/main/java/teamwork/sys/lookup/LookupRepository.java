package teamwork.sys.lookup;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface LookupRepository extends CrudRepository<Lookup, Long>, JpaSpecificationExecutor<Lookup> {

    List<Lookup> findByType(String type);

    Optional<Lookup> findByTypeAndValue(String type, String value);

    @Modifying
    @Query("delete Lookup where id in (?1)")
    void deleteAllById(Iterable<Long> ids);

}
