package teamwork.sys.auth.role.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import teamwork.common.BeanMerger;
import teamwork.sys.auth.role.Role;
import teamwork.sys.auth.role.RoleService;

@RestController
class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/sys/roles")
    public Page<ResponseOverview> roles(RequestQuery req, Pageable pageable) {
        return roleService.query(req, pageable).map(role -> BeanMerger.from(role).exclude("users", "resources").copyTo(new ResponseOverview()));
    }

    @GetMapping("/sys/role/{id}")
    public ResponseOverview detail(@PathVariable("id") String id) {
        return roleService.findById(id).map(role -> BeanMerger.from(role).exclude("users", "resources").copyTo(new ResponseOverview()))
                .orElse(null);
    }

    @PostMapping({"/sys/role/add", "/sys/role/update"})
    public void save(@RequestBody @Validated RequestCreate req) {
        Role role = roleService.findById(req.getId())
                .orElse(new Role());

        roleService.save(BeanMerger.from(req).exclude("users", "resources").copyTo(role));
    }
}
