package teamwork.sys.services;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import teamwork.sys.models.Role;

import java.util.List;

public interface AuthService extends ClientDetailsService, AuthorizationServerTokenServices, ResourceServerTokenServices {

    List<Role> findAllRolesWithResources();

}
