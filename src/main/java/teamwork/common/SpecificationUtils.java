package teamwork.common;

import org.springframework.util.StringUtils;
import teamwork.App;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Optional;

public abstract class SpecificationUtils {

    private static final char FEATURE_NOT = '!';
    private static final char FEATURE_LIKE = '*';

    public static Optional<Predicate> stringCondition(Expression<String> path, String value, CriteriaBuilder criteriaBuilder) {
        value = StringUtils.trimWhitespace(value);

        return equalOrLike(path, value, criteriaBuilder);
    }

    public static Optional<Predicate> numberCondition(Expression<Number> path, Number value, CriteriaBuilder criteriaBuilder) {
        if (value != null) {
            return Optional.of(criteriaBuilder.equal(path, value));
        }
        return Optional.empty();
    }

    public static Optional<Predicate> booleanCondition(Expression<Boolean> path, Boolean value, CriteriaBuilder criteriaBuilder) {
        if (value != null) {
            Predicate result = criteriaBuilder.isTrue(path);

            if (!value) {
                result.not();
            }

            return Optional.of(result);
        }

        return Optional.empty();
    }

    public static Optional<Predicate> booleanCondition(Expression<Boolean> path, Number value, CriteriaBuilder criteriaBuilder) {
        if (value != null) {
            return booleanCondition(path, !value.equals(0), criteriaBuilder);
        }
        return Optional.empty();
    }

    public static Optional<Predicate> booleanCondition(Expression<Boolean> path, String value, CriteriaBuilder criteriaBuilder) {
        if (Boolean.valueOf(value)) {
            return booleanCondition(path, true, criteriaBuilder);
        }
        return Optional.empty();
    }

    private static Optional<Predicate> equalOrLike(Expression<String> path, String value, CriteriaBuilder criteriaBuilder) {

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
            result = criteriaBuilder.like(path, value);
        } else {
            result = criteriaBuilder.equal(path, value);
        }

        if (not) {
            result.not();
        }

        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.debug("query condition {}={},not={},like={}", path, value, not, like);
        }

        return Optional.ofNullable(result);
    }


}
