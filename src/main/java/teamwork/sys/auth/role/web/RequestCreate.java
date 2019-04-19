package teamwork.sys.auth.role.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import teamwork.sys.auth.resource.Resource;
import teamwork.sys.auth.role.Role;
import teamwork.sys.auth.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

class RequestCreate extends Role {

    @NotEmpty
    @Size(min = 1, max = 32)
    @Override
    public String getId() {
        return super.getId();
    }

    @NotEmpty
    @Size(min = 1, max = 64)
    @Override
    public String getDescTxt() {
        return super.getDescTxt();
    }

    @JsonIgnore
    @Override
    public void setUsers(Set<User> users) {
        super.setUsers(users);
    }

    @JsonIgnore
    @Override
    public void setResources(Set<Resource> resources) {
        super.setResources(resources);
    }
}
