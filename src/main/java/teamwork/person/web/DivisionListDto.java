package teamwork.person.web;

import com.fasterxml.jackson.annotation.JsonProperty;

class DivisionListDto {
    private Long id;
    private String name;
    private Boolean inactive;
    private String inactiveTxt;
    private Boolean partner;
    private String parentTxt;

    public DivisionListDto() {}

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

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public String getInactiveTxt() {
        return inactiveTxt;
    }

    public void setInactiveTxt(String inactiveTxt) {
        this.inactiveTxt = inactiveTxt;
    }

    @JsonProperty("is_partner")
    public Boolean getPartner() {
        return partner;
    }

    public void setPartner(Boolean partner) {
        this.partner = partner;
    }

    @JsonProperty("is_partner_txt")
    public String getParentTxt() {
        return parentTxt;
    }

    public void setParentTxt(String parentTxt) {
        this.parentTxt = parentTxt;
    }
}