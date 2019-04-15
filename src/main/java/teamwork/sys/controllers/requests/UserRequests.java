package teamwork.sys.controllers.requests;

import org.springframework.data.jpa.domain.Specification;
import teamwork.common.SpecificationUtils;
import teamwork.sys.models.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.*;
import java.util.List;

public class UserRequests {

    public static class Create {
        @NotBlank
        private String username;
        @Size(min = 1, max = 32)
        private String password;
        @PositiveOrZero
        private Long person_id = 0L;

        public Create() {}

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Long getPerson_id() {
            return person_id;
        }

        public void setPerson_id(Long person_id) {
            this.person_id = person_id;
        }
    }

    public static class Update {
        @NotNull
        @Positive
        private Long id;
        @Size(min = 1, max = 32)
        private String password;
        @PositiveOrZero
        private Long person_id;

        public Update() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Long getPerson_id() {
            return person_id;
        }

        public void setPerson_id(Long person_id) {
            this.person_id = person_id;
        }
    }

    public static class Delete {
        @NotEmpty
        private List<Long> id;

        public List<Long> getId() {
            return id;
        }

        public void setId(List<Long> id) {
            this.id = id;
        }
    }

    public static class Query implements Specification<User> {

        private String username;

        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            SpecificationUtils.equalOrLike(root.get("username"), username, root, query, criteriaBuilder).ifPresent(query::where);
            return query.getRestriction();
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
