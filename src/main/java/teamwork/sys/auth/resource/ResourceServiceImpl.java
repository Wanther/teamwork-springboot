package teamwork.sys.auth.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import teamwork.common.ServiceException;

@Service
class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceRepository resourceRepo;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Page<Resource> query(Specification<Resource> condition, Pageable pageable) throws ServiceException {
        return resourceRepo.findAll(condition, pageable);
    }
}
