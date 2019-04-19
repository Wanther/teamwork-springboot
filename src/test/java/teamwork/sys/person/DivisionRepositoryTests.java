package teamwork.sys.person;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import teamwork.common.TestBase;
import teamwork.person.DivisionRepository;
import teamwork.person.models.Division;

import java.util.List;

public class DivisionRepositoryTests extends TestBase {
    @Autowired
    private DivisionRepository divisionRepository;

    @Test
    public void testFindRootDivision() {
        Division division = divisionRepository.findRootDivision();
        Assert.assertEquals(division.getId(), Long.valueOf(1));
        Assert.assertEquals("沃未来派", division.getName());
    }

    @Test
    public void testFindAllChildren() {
        List<Division> children = divisionRepository.findAllByParentId(1L);
        Assert.assertNotEquals(0, children.size());
    }
}
