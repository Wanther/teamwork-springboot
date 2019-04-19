package teamwork.sys.lookup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class LookupServiceTests {

    @Autowired
    private LookupService lookupService;

    @Test
    public void testCreateAndDelete() {
        Lookup lookup = new Lookup();
        lookup.setType("gender");
        lookup.setValue("3");
        lookup.setText("");
        lookup.setInactive(false);

        lookup = lookupService.save(lookup);

    }

}
