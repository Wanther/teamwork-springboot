package teamwork.person;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.person.models.Division;

import java.util.List;

@Repository
public interface DivisionRepository extends CrudRepository<Division, Long>, JpaSpecificationExecutor<Division> {

    @Modifying
    @Query("delete Division where id in (?1)")
    void deleteAllById(Iterable<Long> ids);

    @Query(
            value = "select p.*, d.* from crm_division d inner join crm_party p on d.id=p.id where d.pid=0",
            nativeQuery = true
    )
    Division findRootDivision();

    List<Division> findAllByParentId(Long parentId);
}
