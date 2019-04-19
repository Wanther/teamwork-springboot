package teamwork.person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import teamwork.person.models.Division;
import teamwork.sys.auth.user.User;

import java.util.List;
import java.util.Optional;

public interface DivisionService {
    Page<Division> query(Specification<Division> condition, Pageable pageable);

    Iterable<Division> findAll();

    Optional<Division> findById(Long id);

    Division getUserDivision(User user);

    List<Division> findChildren(Long parentId);

    Division save(Division division);

    Division save(Division division, Long parentId);

    void deleteByIds(Iterable<Long> ids);

    void deleteById(Long id);
}
