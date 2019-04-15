package teamwork.sys.repos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import teamwork.sys.models.Lookup;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class LookupRepoTests {

    @Autowired
    private LookupRepository lookupRepo;

    @Test
    public void testCreateAndDelete() {
        var lookup = new Lookup();
        lookup.setType("gender");
        lookup.setText("双性");
        lookup.setValue("3");

        lookup = lookupRepo.save(lookup);

        Lookup savedLookup = lookupRepo.findById(lookup.getId()).orElseThrow();

        savedLookup.setText("双性2");

        lookupRepo.save(savedLookup);

        savedLookup = lookupRepo.findById(savedLookup.getId()).orElseThrow();

        lookupRepo.delete(savedLookup);

        Assert.assertEquals(savedLookup.getText(), "双性2");
    }
}
