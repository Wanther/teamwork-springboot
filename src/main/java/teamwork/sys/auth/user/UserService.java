package teamwork.sys.auth.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import teamwork.sys.auth.user.User;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    void delete(Long[] ids);

    User save(User user);
}
