package teamwork.person;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teamwork.person.models.Party;

@Repository
public interface PartyRepository extends org.springframework.data.repository.Repository<Party, Long> {
    @Modifying
    @Query("delete Party where id in (?1)")
    void deleteAllById(Iterable<Long> ids);
}
