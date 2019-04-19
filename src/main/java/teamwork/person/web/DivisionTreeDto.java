package teamwork.person.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivisionTreeDto {
    private Long id;
    private String name;
    private Boolean isLeaf;
    private List<DivisionTreeDto> children;

    public DivisionTreeDto() {}

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

    @JsonProperty("is_leaf")
    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public List<DivisionTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<DivisionTreeDto> children) {
        this.children = children;
    }
}
