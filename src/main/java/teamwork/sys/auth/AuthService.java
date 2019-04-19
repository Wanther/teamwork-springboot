package teamwork.sys.auth;

import teamwork.sys.auth.oauth.OAuthTokenService;
import teamwork.sys.auth.role.Role;

import java.util.List;

public interface AuthService extends OAuthTokenService {

    List<Role> findAllRolesWithResources();

}
