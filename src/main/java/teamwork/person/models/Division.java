package teamwork.person.models;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "crm_division")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@DiscriminatorValue("1")
public class Division extends Party {
    @Column(name = "is_org")
    private Boolean orgnization = false;
    @Column(name = "is_partner")
    private Boolean partner = false;
    @Column(name = "is_leaf")
    private Boolean leaf = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", referencedColumnName = "id", nullable = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Division parent;

    @OneToMany(mappedBy = "division")
    private Set<Person> persons;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.REMOVE})
    private Set<Division> children;

    public Boolean getOrgnization() {
        return orgnization;
    }

    public void setOrgnization(Boolean orgnization) {
        this.orgnization = orgnization;
    }

    public Boolean getPartner() {
        return partner;
    }

    public void setPartner(Boolean partner) {
        this.partner = partner;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Division getParent() {
        return parent;
    }

    public void setParent(Division parent) {
        this.parent = parent;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Set<Division> getChildren() {
        return children;
    }

    public void setChildren(Set<Division> children) {
        this.children = children;
    }

    public void addChild(Division child) {
        if (CollectionUtils.isEmpty(getChildren())) {
            setChildren(new HashSet<>());
        }
        getChildren().add(child);
        child.setParent(this);
        setLeaf(false);
    }

    public void removeChild(Division child) {
        if (!CollectionUtils.isEmpty(getChildren())) {
            getChildren().remove(child);
            child.setParent(null);

            if (getChildren().size() <= 0) {
                setLeaf(true);
            }
        }
    }
}
