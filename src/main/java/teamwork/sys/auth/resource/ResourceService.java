package teamwork.sys.auth.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import teamwork.common.ServiceException;

public interface ResourceService {
    Page<Resource> query(Specification<Resource> condition, Pageable pageable) throws ServiceException;
}
