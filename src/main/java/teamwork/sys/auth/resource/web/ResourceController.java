package teamwork.sys.auth.resource.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import teamwork.App;
import teamwork.common.BeanMerger;
import teamwork.sys.auth.resource.ResourceService;
import teamwork.sys.lookup.LookupService;


@RestController
class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private LookupService lookupService;

    @GetMapping("/sys/resources")
    public Page<ResponseOverview> query(RequestQuery req, Pageable pageable) {
        return resourceService.query(req, pageable).map(resource -> {
            ResponseOverview overview = BeanMerger.from(resource)
                    .exclude("roles")
                    .copyTo(new ResponseOverview());
            overview.setTypeTxt(lookupService.lookup(App.Lookup.SYS_RESOURCE_TYPE, String.valueOf(overview.getType())));
            return overview;
        });
    }
}
