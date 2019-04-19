package teamwork.sys.lookup.web;

import org.springframework.data.jpa.domain.Specification;
import teamwork.sys.lookup.Lookup;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

import static teamwork.common.SpecificationUtils.booleanCondition;
import static teamwork.common.SpecificationUtils.stringCondition;

class RequestQuery extends Lookup implements Specification<Lookup> {

    @Override
    public Predicate toPredicate(Root<Lookup> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> conditions = new LinkedList<>();

        stringCondition(root.get("type"), getType(), criteriaBuilder).ifPresent(conditions::add);
        stringCondition(root.get("value"), getValue(), criteriaBuilder).ifPresent(conditions::add);
        stringCondition(root.get("text"), getText(), criteriaBuilder).ifPresent(conditions::add);
        booleanCondition(root.get("inactive"), getInactive(), criteriaBuilder).ifPresent(conditions::add);

        return query.where(conditions.toArray(new Predicate[0])).getRestriction();
    }
}
