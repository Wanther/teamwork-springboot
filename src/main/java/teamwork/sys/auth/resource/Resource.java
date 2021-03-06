package teamwork.sys.auth.resource;

import teamwork.common.BaseEntity;
import teamwork.sys.auth.role.Role;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sys_resource")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("1")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
public class Resource extends BaseEntity<String> {

    @Id
    private String id;
    @Column(insertable = false, updatable = false)
    private Integer type;
    private String descTxt;

    @ManyToMany(mappedBy = "resources", cascade = CascadeType.REMOVE)
    private Set<Role> roles;

    public Resource() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescTxt() {
        return descTxt;
    }

    public void setDescTxt(String descTxt) {
        this.descTxt = descTxt;
    }
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Entity
    @DiscriminatorValue("2")
    public static class Menu extends Resource {

    }
}
