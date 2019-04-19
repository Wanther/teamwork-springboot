package teamwork.common;

import org.hibernate.Session;
import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import teamwork.App;
import teamwork.sys.auth.user.User;

import java.util.Optional;

public class CreationUidGeneration implements AnnotationValueGeneration<CreationUid> {
    @Override
    public void initialize(CreationUid annotation, Class<?> propertyType) {

    }

    @Override
    public GenerationTiming getGenerationTiming() {
        return GenerationTiming.INSERT;
    }

    @Override
    public ValueGenerator<?> getValueGenerator() {
        return new AuditorGenerator();
    }

    @Override
    public boolean referenceColumnInSql() {
        return false;
    }

    @Override
    public String getDatabaseGeneratedReferencedColumnValue() {
        return null;
    }

    public static class AuditorGenerator implements ValueGenerator<Long> {

        @Override
        public Long generateValue(Session session, Object owner) {
            return getAuditor().map(User::getId).orElse(0L);
        }

        private Optional<User> getAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof User) {
                    return Optional.of((User) principal);
                }
            }
            if (App.LOGGER.isDebugEnabled()) {
                App.LOGGER.warn("auditor not found");
            }

            return Optional.empty();
        }
    }


}
