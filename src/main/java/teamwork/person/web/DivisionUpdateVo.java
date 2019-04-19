package teamwork.person.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

class DivisionUpdateVo {
    @NotNull
    @Positive
    private Long id;
    private String name;
    private Boolean partner;
    private Long parentId;

    public DivisionUpdateVo() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPartner() {
        return partner;
    }

    @JsonProperty("is_partner")
    public void setPartner(Boolean partner) {
        this.partner = partner;
    }

    public Long getParentId() {
        return parentId;
    }

    @JsonProperty("pid")
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
