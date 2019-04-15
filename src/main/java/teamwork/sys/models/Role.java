package teamwork.sys.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.core.GrantedAuthority;
import teamwork.common.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonView(User.View.Basic.class)
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String descTxt;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.REMOVE)
    private Set<User> users;

    @JsonView(User.View.Detail.class)
    @JsonManagedReference
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

    @JsonIgnore
    @Override
    public String getAuthority() {
        return "ROLE_" + getId();
    }
}
