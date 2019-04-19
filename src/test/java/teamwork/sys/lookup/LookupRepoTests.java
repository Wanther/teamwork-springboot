package teamwork.sys.lookup;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class LookupRepoTests {

    @Autowired
    private LookupRepository lookupRepo;

    @Test
    public void testCreateAndDelete() {
        Lookup lookup = new Lookup();
        lookup.setType("gender");
        lookup.setText("双性");
        lookup.setValue("3");

        lookup = lookupRepo.save(lookup);

        Lookup savedLookup = lookupRepo.findById(lookup.getId()).orElseThrow(RuntimeException::new);

        savedLookup.setText("双性2");

        lookupRepo.save(savedLookup);

        savedLookup = lookupRepo.findById(savedLookup.getId()).orElseThrow(RuntimeException::new);

        lookupRepo.delete(savedLookup);

        Assert.assertEquals(savedLookup.getText(), "双性2");
    }
}
