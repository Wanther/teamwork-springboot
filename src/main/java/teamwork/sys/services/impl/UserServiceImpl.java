package teamwork.sys.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamwork.sys.models.User;
import teamwork.sys.repos.UserRepository;
import teamwork.sys.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findRolesByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return userRepo.findRolesById(id).orElseThrow(() -> new UsernameNotFoundException("id=" + id));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Transactional
    @Override
    public void delete(Long[] ids) {
        userRepo.deleteByIds(ids);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }
}
