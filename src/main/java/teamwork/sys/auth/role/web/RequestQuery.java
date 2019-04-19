package teamwork.sys.auth.role.web;

import org.springframework.data.jpa.domain.Specification;
import teamwork.sys.auth.role.Role;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.LinkedList;
import java.util.List;

import static teamwork.common.SpecificationUtils.stringCondition;

class RequestQuery extends Role implements Specification<Role> {

    @Override
    public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> conditions = new LinkedList<>();

        stringCondition(root.get("id"), getId(), criteriaBuilder).ifPresent(conditions::add);

        stringCondition(root.get("descTxt"), getDescTxt(), criteriaBuilder).ifPresent(conditions::add);

        return query.where(conditions.toArray(new Predicate[0])).getRestriction();
    }

}
