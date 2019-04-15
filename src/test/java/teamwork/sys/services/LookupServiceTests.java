package teamwork.sys.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import teamwork.sys.models.Lookup;
import teamwork.sys.repos.LookupRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class LookupServiceTests {

    @Autowired
    private LookupService lookupService;

    @Autowired
    private LookupRepository lookupRepo;

    @Test
    public void testLookupByType() {
        final String gender = "gender";
        var lookups = lookupService.lookup(gender);

        Assert.assertEquals(3, lookups.size());
    }

    @Test
    public void testCreateAndDelete() {
        var lookup = new Lookup();
        lookup.setType("gender");
        lookup.setValue("3");
        lookup.setText("");
        lookup.setInactive(false);

        lookup = lookupService.save(lookup);

        Lookup lookup2 = lookupRepo.findById(lookup.getId()).orElseThrow();

        lookupRepo.delete(lookup2);

    }

}
