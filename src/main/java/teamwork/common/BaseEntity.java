package teamwork.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@MappedSuperclass
public abstract class BaseEntity<PK extends Serializable> implements Serializable {

    public static final Collection<String> SYSTEM_FIELDS = Collections.unmodifiableSet(Set.of("created", "createdBy", "updated", "updatedBy"));

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(updatable = false)
    @LazyGroup("audit")
    @CreationTimestamp
    private LocalDateTime created;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @Column(updatable = false)
    @LazyGroup("audit")
    @CreationUid
    private Long createdBy;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("audit")
    @UpdateTimestamp
    private LocalDateTime updated;

    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("audit")
    @UpdateUid
    private Long updatedBy;

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
}
