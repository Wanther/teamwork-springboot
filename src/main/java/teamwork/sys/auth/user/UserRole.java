package teamwork.sys.auth.user;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyGroup;
import teamwork.common.CreationUid;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sys_user_role")
public class UserRole implements Serializable {

    public static UserRole of(@NotNull Long userId, @NotNull String roleId) {
        UserRole ur = new UserRole();
        ur.userId = userId;
        ur.roleId = roleId;
        return ur;
    }

    @Id
    @Column(name = "uid")
    private Long userId;
    @Id
    @Column(name = "role_id")
    private String roleId;

    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("audit")
    @CreationTimestamp
    private LocalDateTime created;

    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("audit")
    @CreationUid
    private Long createdBy;

    public UserRole() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(userId, userRole.userId) &&
                Objects.equals(roleId, userRole.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
