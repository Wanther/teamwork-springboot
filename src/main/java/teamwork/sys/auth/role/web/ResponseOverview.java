package teamwork.sys.auth.role.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import teamwork.sys.auth.resource.Resource;
import teamwork.sys.auth.role.Role;
import teamwork.sys.auth.user.User;

import java.util.Set;

class ResponseOverview extends Role {

    @JsonIgnore
    @Override
    public Integer getLevel() {
        return super.getLevel();
    }

    @JsonIgnore
    @Override
    public Set<User> getUsers() {
        return super.getUsers();
    }

    @JsonIgnore
    @Override
    public Set<Resource> getResources() {
        return super.getResources();
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return super.getAuthority();
    }
}
