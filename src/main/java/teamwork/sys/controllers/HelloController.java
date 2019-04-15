package teamwork.sys.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import teamwork.App;
import teamwork.sys.models.Resource;
import teamwork.sys.models.Role;
import teamwork.sys.repos.ResourceRepository;
import teamwork.sys.repos.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private RoleRepository roleRepo;

    @GetMapping("/sys/resources")
    @Transactional
    public List<Resource> resources() {
        return resourceRepo.findAll();
    }

    @GetMapping("/sys/resource/{id}")
    @Transactional
    public Resource resource(@PathVariable("id") String id) {
        return resourceRepo.findById(id).orElseThrow();
    }

    @GetMapping("/sys/role/{id}")
    public Role role(@PathVariable("id") String id) {
        return roleRepo.findById(id).orElseThrow();
    }

    @GetMapping("/test/params")
    public void receiveParameters(NativeWebRequest webRequest) {
        App.LOGGER.debug(webRequest.getParameterMap().toString());
    }

    @GetMapping("/test/pageable")
    public void pageAndSort(Pageable page) {
        App.LOGGER.debug("{}", page);
    }
}
