package teamwork.sys.person.services;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import teamwork.App;
import teamwork.common.TestBase;
import teamwork.person.DivisionService;
import teamwork.person.PersonService;
import teamwork.person.models.Division;
import teamwork.person.models.Person;

import java.time.LocalDateTime;
import java.util.Map;

public class PersonServiceTests extends TestBase {

    @Autowired
    private PersonService personService;
    @Autowired
    private DivisionService divisionService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testQuery() {
        personService.query((root, query, criteriaBuilder) -> query.getRestriction(), PageRequest.of(0, 30))
                .forEach(person -> App.LOGGER.debug(person.toString()));
    }

    @Test
    @Transactional
    public void testGetChildren() {
        App.LOGGER.debug("=====================================");
        personService.findById(5L).ifPresent(person -> {
            App.LOGGER.debug("[{}]{}", person.getId(), person.getName());
            App.LOGGER.debug("d[{}]{}", person.getDivision().getId(), person.getDivision().getName());
        });

        App.LOGGER.debug("=====================================");
    }

    @Test
    public void testSave() {
        Person person = new Person();
        person.setCreated(LocalDateTime.now());
        person.setUpdated(LocalDateTime.now());
        person.setCreatedBy(1L);
        person.setUpdatedBy(1L);
        person.setName("测试1");
        person.setInactive(false);
        person.setGender(1);

        Division division = divisionService.findById(1L).orElseThrow();
        person.setDivision(division);

        person = personService.save(person);

        Map<String, Object> saveResult = jdbcTemplate.queryForMap("select * from crm_person d inner join crm_party p on d.id=p.id and d.id=?", person.getId());

        Assert.assertEquals(person.getName(), saveResult.get("name"));

        jdbcTemplate.update("delete from crm_division where id=?", person.getId());
        jdbcTemplate.update("delete from crm_person where id=?", person.getId());

    }
}
