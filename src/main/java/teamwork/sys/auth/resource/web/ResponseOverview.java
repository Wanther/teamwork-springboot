package teamwork.sys.auth.resource.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import teamwork.sys.auth.resource.Resource;
import teamwork.sys.auth.role.Role;

import java.util.Set;

class ResponseOverview extends Resource {
    private String typeTxt;

    @JsonIgnore
    @Override
    public Set<Role> getRoles() {
        return super.getRoles();
    }

    public String getTypeTxt() {
        return typeTxt;
    }

    public void setTypeTxt(String typeTxt) {
        this.typeTxt = typeTxt;
    }
}
