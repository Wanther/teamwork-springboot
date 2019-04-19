package teamwork.sys.auth.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAllWithResources();

    Page<Role> query(Specification<Role> condition, Pageable pageable);

    Optional<Role> findById(String id);

    Role save(Role role);

    void delete(Iterable<String> ids);
}
