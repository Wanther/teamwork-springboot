package teamwork.person.web;

import com.fasterxml.jackson.annotation.JsonProperty;

class DivisionDetailDto extends DivisionListDto {
    private String parentName;
    private Long parentId;

    public DivisionDetailDto() {}

    public String getParentName() {
        return parentName;
    }

    @JsonProperty("pname")
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @JsonProperty("pid")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
