package teamwork.sys.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamwork.common.ServiceException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
class LookupServiceImpl implements LookupService {

    @Autowired
    private LookupRepository lookupRepo;

    @Cacheable("c1")
    @Override
    public String lookup(@NotNull String type, @NotNull String value) {
        return lookupRepo.findByTypeAndValue(type, value).map(Lookup::getText).orElse(value);
    }

    @Override
    public Lookup save(Lookup lookup) throws ServiceException{
        return lookupRepo.save(lookup);
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) throws ServiceException {
        lookupRepo.deleteAllById(ids);
    }

    @Override
    public Page<Lookup> query(Specification<Lookup> condition, Pageable pageable) throws ServiceException {
        return lookupRepo.findAll(condition, pageable);
    }

    @Override
    public List<Lookup> query(Specification<Lookup> condition) throws ServiceException {
        return lookupRepo.findAll(condition);
    }

    @Override
    public Optional<Lookup> findById(Long id) throws ServiceException {
        return lookupRepo.findById(id);
    }
}
