package teamwork.person.web;

import org.hibernate.criterion.Restrictions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import teamwork.person.models.Division;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

import static teamwork.common.SpecificationUtils.booleanCondition;
import static teamwork.common.SpecificationUtils.stringCondition;

class DivisionQueryVo implements Specification<Division> {
    private String name;
    private Boolean partner;
    private Boolean inactive;

    public DivisionQueryVo() {}

    @Override
    public Predicate toPredicate(Root<Division> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> conditions = new LinkedList<>();
        stringCondition(root.get("name"), name, criteriaBuilder).ifPresent(conditions::add);
        booleanCondition(root.get("inactive"), inactive, criteriaBuilder).ifPresent(conditions::add);
        booleanCondition(root.get("partner"), partner, criteriaBuilder);
        return query.where(conditions.toArray(new Predicate[0])).getRestriction();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPartner() {
        return partner;
    }

    public void setPartner(Boolean partner) {
        this.partner = partner;
    }

    public Boolean getIs_partner() {
        return getPartner();
    }

    public void setIs_partner(Boolean is_partner) {
        setPartner(is_partner);
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(String yn) {
        setInactive("Y".equals(yn));
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }
}
