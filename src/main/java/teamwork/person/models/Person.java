package teamwork.person.models;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;

@Entity
@Table(name = "crm_person")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@DiscriminatorValue("2")
public class Person extends Party {
    private Integer gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "div_id", referencedColumnName = "id", nullable = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Division division;

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }
}
