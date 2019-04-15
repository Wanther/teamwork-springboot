package teamwork.common;

import org.springframework.util.StringUtils;
import teamwork.App;

import javax.persistence.criteria.*;
import java.util.Optional;

public abstract class SpecificationUtils {

    private static final char FEATURE_NOT = '!';
    private static final char FEATURE_LIKE = '*';

    public static <T> Optional<Predicate> equalOrLike(Expression<String> path, String value, Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }

        Predicate result;

        final boolean not = value.indexOf(FEATURE_NOT) == 0;
        final boolean like = value.indexOf(FEATURE_LIKE) >= 0;

        if (not) {
            value = value.substring(1);
        }

        if (like) {
            value = value.replace(FEATURE_LIKE, '%');
        }

        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.debug("query condition {}={},not={},like={}", path, value, not, like);
        }

        if (like) {
            if (not) {
                result = criteriaBuilder.notLike(path, value);
            } else {
                result = criteriaBuilder.like(path, value);
            }
        } else {
            if (not) {
                result = criteriaBuilder.notEqual(path, value);
            } else {
                result = criteriaBuilder.equal(path, value);
            }
        }

        return Optional.ofNullable(result);
    }
}
