package teamwork.person.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import teamwork.App;
import teamwork.common.BeanMerger;
import teamwork.common.Requests;
import teamwork.common.ServiceException;
import teamwork.person.DivisionService;
import teamwork.person.models.Division;
import teamwork.sys.auth.user.User;
import teamwork.sys.lookup.LookupService;

import java.util.LinkedList;
import java.util.List;

@RestController
class DivisionController {

    @Autowired private DivisionService divisionService;
    @Autowired private LookupService lookupService;

    @GetMapping("/divisions")
    public Page<DivisionListDto> query(DivisionQueryVo req, Pageable pageable) {
        return divisionService.query(req, pageable).map(division -> {
            DivisionListDto listDto = BeanMerger.from(division)
                    .copyTo(new DivisionListDto());
            listDto.setInactiveTxt(lookupService.lookup(App.Lookup.BOOLEAN_YN, String.valueOf(division.getInactive() ? 1 : 0)));
            listDto.setParentTxt(lookupService.lookup(App.Lookup.BOOLEAN_YN, String.valueOf(division.getPartner() ? 1 : 0)));
            return listDto;
        });
    }

    @Transactional(readOnly = true)
    @GetMapping("/division/{id}")
    public DivisionDetailDto detail(@PathVariable("id") Long id) {
        return divisionService.findById(id).map(division -> {
            DivisionDetailDto detailDto = BeanMerger.from(division)
                    .exclude("persons", "children", "organization", "parent")
                    .copyTo(new DivisionDetailDto());
            if (division.getParent() != null) {
                detailDto.setParentName(division.getParent().getName());
                detailDto.setParentId(division.getParent().getId());
            }

            return detailDto;

        }).orElseThrow(() -> new ServiceException("notExist.division"));
    }

    @Transactional(readOnly = true)
    @GetMapping("/division/children/{pid}")
    public List<DivisionTreeDto> children(@PathVariable("pid") Long pid, @AuthenticationPrincipal User user) {


        List<DivisionTreeDto> children = new LinkedList<>();

        if (pid == 0L) {
            Division userDivision = divisionService.getUserDivision(user);
            DivisionTreeDto divisionTreeDto = BeanMerger.from(userDivision)
                    .include("id", "name", "leaf")
                    .copyTo(new DivisionTreeDto());

            if (!divisionTreeDto.getLeaf()) {
                divisionTreeDto.setChildren(new LinkedList<>());
            }

            children.add(divisionTreeDto);

            return children;
        }

        divisionService.findChildren(pid).forEach(division -> {
            DivisionTreeDto childTreeDto = BeanMerger.from(division)
                    .include("id", "name", "leaf")
                    .copyTo(new DivisionTreeDto());

            if (!childTreeDto.getLeaf()) {
                childTreeDto.setChildren(new LinkedList<>());
            }

            children.add(childTreeDto);
        });

        return children;
    }

    @Transactional
    @PostMapping("/division/add")
    public void create(@Validated @RequestBody DivisionCreateVo req) {

        divisionService.save(BeanMerger.from(req).copyTo(new Division()), req.getParentId());
    }

    @Transactional
    @PostMapping("/division/update")
    public void update(@Validated @RequestBody DivisionUpdateVo req) {
        Division division = divisionService.findById(req.getId()).orElseThrow(() -> new ServiceException("notExist.division"));
        divisionService.save(BeanMerger.from(req).copyTo(division), req.getParentId());
    }

    @PostMapping("/division/delete")
    public void delete(@Validated @RequestBody Requests.BatchDelete req) {
        divisionService.deleteByIds(req.getId());
    }
}
