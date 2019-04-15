package teamwork.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyGroup;
import org.springframework.security.core.context.SecurityContextHolder;
import teamwork.App;
import teamwork.sys.models.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@MappedSuperclass
public abstract class BaseEntity<PK extends Serializable> implements Serializable {

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(updatable = false)
    @LazyGroup("audit")
    private LocalDateTime created;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(updatable = false)
    @LazyGroup("audit")
    private Long createdBy;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("audit")
    private LocalDateTime updated;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("audit")
    private Long updatedBy;

    @PrePersist
    public void touchOnCreate() {

        LocalDateTime now = LocalDateTime.now().withNano(0);
        Long auditId = getAuditor().map(User::getId)
                .or(() -> Optional.ofNullable(createdBy))
                .or(() -> Optional.ofNullable(updatedBy))
                .orElse(0L);

        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.debug("touchOnCreate {}:{}-{}", getClass(), now, auditId);
        }

        created = now;
        createdBy = auditId;

        updated = now;
        updatedBy = auditId;
    }

    @PreUpdate
    public void touchOnUpdate() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Long auditId = getAuditor().map(User::getId).orElse(0L);

        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.debug("touchOnUpdate {}:{}-{}", getClass(), now, auditId);
        }

        updated = now;
        updatedBy = auditId;
    }

    public abstract PK getId();
    public abstract void setId(PK id);

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity other = (BaseEntity) o;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    private Optional<User> getAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return Optional.of((User) principal);
        }

        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.warn("auditor not found, principal={}", principal);
        }

        return Optional.empty();
    }
}
