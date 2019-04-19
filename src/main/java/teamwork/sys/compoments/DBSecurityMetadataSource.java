package teamwork.sys.compoments;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import teamwork.sys.auth.role.Role;
import teamwork.sys.auth.role.RoleService;

import java.util.*;

//TODO:
public class DBSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final RoleService roleService;

    public DBSecurityMetadataSource(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        List<Role> roles = roleService.findAllWithResources();

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> mapping = new LinkedHashMap<>();

        roles.forEach(role ->
                Optional.ofNullable(role.getResources()).ifPresent(resources ->
                        resources.forEach(resource -> {
                            Arrays.stream(resource.getDescTxt().split(",")).map(AntPathRequestMatcher::new).forEach(requestMatcher -> {
                                Collection<ConfigAttribute> configAttributes = Optional.ofNullable(mapping.get(requestMatcher)).orElse(new HashSet<>());
                                configAttributes.add(new SecurityConfig(role.getAuthority()));
                            });
                        })));
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
