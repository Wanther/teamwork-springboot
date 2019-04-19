package teamwork.sys.lookup.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import teamwork.App;
import teamwork.common.BeanMerger;
import teamwork.common.Requests;
import teamwork.common.ServiceException;
import teamwork.sys.lookup.Lookup;
import teamwork.sys.lookup.LookupService;

@RestController
class LookupController {

    @Autowired
    private LookupService lookupService;

    @GetMapping("/sys/lookup/{id}")
    public Lookup detail(@PathVariable("id") Long id) {
        return lookupService.findById(id).orElseThrow(() -> new ServiceException("notExist.record"));
    }

    @PostMapping("/sys/lookup/add")
    public void create(@Validated @RequestBody RequestCreate req) {
        lookupService.save(BeanMerger.from(req).copyTo(new Lookup()));
    }

    @PostMapping("/sys/lookup/edit")
    public void update(@Validated @RequestBody RequestUpdate req) {
        lookupService.save(BeanMerger.from(req).copyTo(new Lookup()));
    }

    @PostMapping("/sys/lookup/delete")
    public void delete(@Validated @RequestBody Requests.BatchDelete req) {
        lookupService.delete(req.getId());
    }

    @GetMapping("/sys/lookups")
    public Page<ResponseOverview> lookups(RequestQuery req, Pageable pageable) {

        return lookupService.query(req, pageable).map(lookup -> {
            ResponseOverview overview = BeanMerger.from(lookup)
                    .copyTo(new ResponseOverview());
            overview.setInactiveTxt(lookupService.lookup(App.Lookup.BOOLEAN_YN, String.valueOf(lookup.getInactive() ? 1 : 0)));
            return overview;
        });
    }
}
