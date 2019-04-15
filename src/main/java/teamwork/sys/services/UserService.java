package teamwork.sys.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import teamwork.sys.models.User;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    void delete(Long[] ids);

    User save(User user);
}
