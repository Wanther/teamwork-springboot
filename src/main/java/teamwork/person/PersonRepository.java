package teamwork.person;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.person.models.Person;

@Repository
interface PersonRepository extends CrudRepository<Person, Long>, JpaSpecificationExecutor<Person> {
}
