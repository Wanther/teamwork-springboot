package teamwork.person.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

class DivisionCreateVo {
    @NotEmpty
    private String name;
    @NotNull
    @Positive
    private Long parentId;
    private Boolean partner;

    public DivisionCreateVo() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    @JsonProperty("pid")
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getPartner() {
        return partner;
    }

    @JsonProperty("is_partner")
    public void setPartner(Boolean partner) {
        this.partner = partner;
    }
}
