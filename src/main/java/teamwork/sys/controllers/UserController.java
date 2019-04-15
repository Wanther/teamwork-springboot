package teamwork.sys.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import teamwork.sys.controllers.requests.UserRequests;
import teamwork.sys.models.User;
import teamwork.sys.repos.UserRepository;
import teamwork.sys.services.UserService;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    @JsonView(User.View.Basic.class)
    public Page<User> query(UserRequests.Query query, Pageable pageable) {

        return userRepository.findAll(query, pageable);

    }

    @PostMapping("/user/add")
    public void create(@Valid UserRequests.Create req, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        if(userService.findByUsername(req.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "alreadyExist");
            throw new BindException(bindingResult);
        }

        if (req.getPassword() == null) {
            req.setPassword(req.getUsername());
        }

        req.setPassword(passwordEncoder.encode(req.getPassword()));

        User user = User.of(req.getUsername(), req.getPassword(), req.getPerson_id());

        userService.save(user);

    }

    @PostMapping("/user/update")
    public void update(@Valid UserRequests.Update req, BindingResult bindingResult) throws BindException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        User dbUser = userService.findById(req.getId()).orElseThrow(() -> {
            bindingResult.reject("notExist");
            return new BindException(bindingResult);
        });

        if (req.getPassword() != null) {
            dbUser.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        if (req.getPerson_id() != null) {
            dbUser.setPersonId(req.getPerson_id());
        }

        userService.save(dbUser);
    }

    @PostMapping("/user/delete")
    public void delete(@RequestBody UserRequests.Delete req) {
        userService.delete(req.getId().toArray(new Long[0]));
    }
}
