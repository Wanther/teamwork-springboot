package teamwork.sys.auth.role;

import org.springframework.security.core.GrantedAuthority;
import teamwork.common.BaseEntity;
import teamwork.sys.auth.resource.Resource;
import teamwork.sys.auth.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity<String> implements GrantedAuthority {

    public static Role of(@NotNull String id, String descTxt) {
        Role role = new Role();
        role.id = id;
        role.descTxt = descTxt;
        return role;
    }

    @Id
    private String id;
    private Integer level = 0;
    private String descTxt;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.REMOVE)
    private Set<User> users;

    @ManyToMany
    @JoinTable(name = "sys_role_res",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "res_id", referencedColumnName = "id"))
    private Set<Resource> resources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescTxt() {
        return descTxt;
    }

    public void setDescTxt(String descTxt) {
        this.descTxt = descTxt;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + getId();
    }
}
