package teamwork.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import teamwork.person.models.Person;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepo;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Page<Person> query(Specification<Person> condition, Pageable pageable) {
        return personRepo.findAll(condition, pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Optional<Person> findById(Long id) {
        return personRepo.findById(id);
    }

    @Transactional
    @Override
    public Person save(Person person) {
        return personRepo.save(person);
    }
}
