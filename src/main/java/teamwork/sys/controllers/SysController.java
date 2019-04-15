package teamwork.sys.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import teamwork.sys.models.Resource;
import teamwork.sys.models.Role;
import teamwork.sys.models.User;
import teamwork.sys.services.AuthService;
import teamwork.sys.services.UserService;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class SysController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("/sys/user/views")
    public Set<Resource> viewsOfUser(@AuthenticationPrincipal User user) {
        List<Role> roles = authService.findAllRolesWithResources();

        final Set<Resource> resources = new HashSet<>();

        roles.forEach(role -> {
            if (user.getRoles().contains(role)) {
                resources.addAll(role.getResources());
            }
        });

        return resources;
    }

    @GetMapping("/sys/user/{userId}/roles")
    @JsonView(User.View.Basic.class)
    public Set<Role> userRoles(@PathVariable("userId") Long userId, @AuthenticationPrincipal User user) throws ValidationException {
        if (userId > 0) {
            user = userService.findById(userId).orElseThrow(() -> new ValidationException("用户不存在"));
        }

        return user.getRoles();
    }
}
