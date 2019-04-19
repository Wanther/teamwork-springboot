package teamwork.sys.auth.web;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import teamwork.common.BeanMerger;
import teamwork.common.Scenes;
import teamwork.person.PersonService;
import teamwork.sys.auth.AuthService;
import teamwork.sys.auth.resource.Resource;
import teamwork.sys.auth.role.Role;
import teamwork.sys.auth.user.User;
import teamwork.sys.auth.user.UserService;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class SysController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private PersonService personService;

    @GetMapping("/sys/user/views")
    public Set<Resource> viewsOfUser(@AuthenticationPrincipal User user) {
        List<Role> roles = authService.findAllRolesWithResources();

        final Set<Resource> resources = new HashSet<>();

        roles.forEach(role -> {
            if (user.getRoles().contains(role)) {
                role.getResources().forEach(resource -> {
                    resources.add(BeanMerger.from(resource)
                            .exclude("roles", "type")
                            .copyTo(new Resource()));
                });
            }
        });

        return resources;
    }

    @Transactional(readOnly = true)
    @GetMapping("/sys/user/{userId}/roles")
    public Set<Role> userRoles(@PathVariable("userId") Long userId, @AuthenticationPrincipal User user) throws ValidationException {
        if (userId > 0) {
            user = userService.findById(userId).orElseThrow(() -> new ValidationException("用户不存在"));
        }

        return user.getRoles().stream().map(role -> BeanMerger.from(role).include("id", "desc_txt").copyTo(new Role())).collect(Collectors.toSet());
    }

    @GetMapping("/sys/my_detail")
    public UserDetailDto userDetail(@AuthenticationPrincipal User user) {
        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setId(user.getId());
        userDetailDto.setUsername(user.getUsername());

        return personService.findById(user.getPersonId()).map(person -> {
            userDetailDto.setPersonId(person.getId());
            userDetailDto.setPersonName(person.getName());
            return userDetailDto;
        }).orElse(userDetailDto);
    }

}
