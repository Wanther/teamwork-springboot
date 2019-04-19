package teamwork.sys.person.services;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import teamwork.App;
import teamwork.common.TestBase;
import teamwork.person.DivisionService;
import teamwork.person.models.Division;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

public class DivisionServiceTests extends TestBase {

    @Autowired
    private DivisionService divisionService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testQuery() {
        divisionService.query((root, query, criteriaBuilder) -> query.getRestriction(), PageRequest.of(0, 30))
                .forEach(division -> App.LOGGER.debug(division.toString()));
    }

    @Test
    public void testFindAll() {
        App.LOGGER.debug("=====================================");
        divisionService.findAll().forEach(division -> {
            App.LOGGER.debug(division.toString());
        });
        App.LOGGER.debug("=====================================");
    }

    @Test
    @Transactional
    public void testGetChildren() {
        App.LOGGER.debug("=====================================");
        divisionService.findById(1L).ifPresent(division -> {
            App.LOGGER.debug("[{}]{}", division.getId(), division.getName());
            if (division.getChildren() != null) {
                division.getChildren().forEach(division1 -> App.LOGGER.debug("\t[{}]{}", division1.getId(), division1.getName()));
            }
        });

        App.LOGGER.debug("=====================================");
    }

    @Transactional
    @Test
    public void testGetParent() {
        divisionService.findById(9L).ifPresent(division -> {
            App.LOGGER.debug("[{}]{}", division.getId(), division.getName());
            Division parent = division.getParent();
            while (parent != null) {
                App.LOGGER.debug("[{}]{}", parent.getId(), parent.getName());
                parent = parent.getParent();
            }
        });
    }

    @Transactional
    @Test
    public void testSave() {

        Division parent = divisionService.findById(1L).orElseThrow();

        Division division = new Division();
        division.setCreated(LocalDateTime.now());
        division.setUpdated(LocalDateTime.now());
        division.setCreatedBy(1L);
        division.setUpdatedBy(1L);
        division.setParent(parent);
        division.setName("测试1");
        division.setLeaf(true);
        division.setOrgnization(false);
        division.setPartner(false);
        division.setInactive(false);

        division = divisionService.save(division);

        Map<String, Object> saveResult = jdbcTemplate.queryForMap("select * from crm_division d inner join crm_party p on d.id=p.id and d.id=?", division.getId());

        Assert.assertEquals(division.getName(), saveResult.get("name"));
        Assert.assertEquals(division.getLeaf(), saveResult.get("is_leaf"));
        Assert.assertEquals(parent, division.getParent());

        divisionService.deleteById(division.getId());

    }

    @Transactional
    @Test
    public void testAddChildren() {
        Division parent = divisionService.findById(1L).orElseThrow();

        Division division = new Division();
        division.setCreated(LocalDateTime.now());
        division.setUpdated(LocalDateTime.now());
        division.setCreatedBy(1L);
        division.setUpdatedBy(1L);
        division.setName("测试1");
        division.setLeaf(true);
        division.setOrgnization(false);
        division.setPartner(false);
        division.setInactive(false);

        division.setParent(parent);
        parent.getChildren().add(division);

        parent = divisionService.save(parent);
    }

    @Transactional
    @Rollback(false)
    @Test
    public void testDelete() {
        Division parent = divisionService.findById(1L).orElseThrow();

        parent.getChildren().removeIf(d -> d.getName().equals("测试1"));

        divisionService.save(parent);
    }

    @Transactional
    @Rollback(false)
    @Test
    public void testMoveNode() {
        Division parent = divisionService.findById(1L).orElseThrow();
        Division parent2 = divisionService.findById(8L).orElseThrow();

        Division division = new Division();
        division.setCreated(LocalDateTime.now());
        division.setUpdated(LocalDateTime.now());
        division.setCreatedBy(1L);
        division.setUpdatedBy(1L);
        division.setName("测试2");
        division.setLeaf(true);
        division.setOrgnization(false);
        division.setPartner(false);
        division.setInactive(false);

        division.setParent(parent);
        parent.getChildren().add(division);

        divisionService.save(division);

        final Long divisionId = division.getId();
        App.LOGGER.debug("divition.id={}", divisionId);

        parent.getChildren().removeIf(d -> d.getId().equals(divisionId));
        division.setParent(null);

        parent2.getChildren().add(division);
        division.setParent(parent2);

        divisionService.save(parent2);

    }
}
