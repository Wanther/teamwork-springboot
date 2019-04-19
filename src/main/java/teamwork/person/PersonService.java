package teamwork.person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import teamwork.person.models.Person;

import java.util.Optional;

public interface PersonService {
    Page<Person> query(Specification<Person> condition, Pageable pageable);
    Optional<Person> findById(Long id);
    Person save(Person person);
}
