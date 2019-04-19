package teamwork.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import teamwork.common.ServiceException;
import teamwork.person.models.Division;
import teamwork.person.models.Person;
import teamwork.sys.auth.user.User;

import java.util.List;
import java.util.Optional;

@Service
public class DivisionServiceImpl implements DivisionService {

    @Autowired
    private PartyRepository partyRepo;
    @Autowired
    private DivisionRepository divisionRepo;
    @Autowired
    private PersonService personService;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Page<Division> query(Specification<Division> condition, Pageable pageable) {
        return divisionRepo.findAll(condition, pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Iterable<Division> findAll() {
        return divisionRepo.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Optional<Division> findById(Long id) {
        return divisionRepo.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Division getUserDivision(User user) {
        if (user == null) {
            throw new ServiceException("notExist.user");
        }

        if (user.isSysAdmin()) {
            return divisionRepo.findRootDivision();
        }

        if (ObjectUtils.nullSafeEquals(user.getPersonId(), 0)){
            throw new AccessDeniedException("personId=0 and not SYS_ADMIN, please assign a person to this user");
        }

        Person person = personService.findById(user.getPersonId()).orElseThrow(() -> new ServiceException("notExist.person"));

        return person.getDivision();
    }

    @Override
    public List<Division> findChildren(Long parentId) {
        return divisionRepo.findAllByParentId(parentId);
    }

    @Transactional
    @Override
    public Division save(Division division) {
        return divisionRepo.save(division);
    }

    @Transactional
    @Override
    public Division save(Division division, Long parentId) {

        Division oldParent = division.getParent();

        Division newParent;
        if (parentId == null || parentId.equals(0L)) {
            newParent = null;
        } else {
            newParent = findById(parentId).orElseThrow(() -> new ServiceException("notExist.division.parent"));
        }

        if (!ObjectUtils.nullSafeEquals(oldParent, newParent)) {

            if (oldParent != null) {
                oldParent.removeChild(division);
                divisionRepo.save(oldParent);
            }

            if (newParent != null) {
                newParent.addChild(division);
                divisionRepo.save(division);
            }
        }

        return divisionRepo.save(division);
    }

    @Transactional
    @Override
    public void deleteByIds(Iterable<Long> ids) {
        divisionRepo.deleteAll(divisionRepo.findAllById(ids));
    }

    @Override
    public void deleteById(Long id) {
        divisionRepo.deleteById(id);
    }
}
