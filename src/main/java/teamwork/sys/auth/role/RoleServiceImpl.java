package teamwork.sys.auth.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Role> findAllWithResources() {
        return roleRepo.findAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Page<Role> query(Specification<Role> condition, Pageable pageable) {
        return roleRepo.findAll(condition, pageable);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Role> findById(String id) {
        return roleRepo.findById(id);
    }

    @Transactional
    @Override
    public Role save(Role role) {
        return roleRepo.save(role);
    }

    @Transactional
    @Override
    public void delete(Iterable<String> ids) {
        roleRepo.deleteAllById(ids);
    }
}
