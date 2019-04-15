package teamwork.sys.repos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class OAuthClientRepoTests {

    @Autowired
    private OAuthClientRepository clientRepo;

    @Test
    public void testGet() {
        var client = clientRepo.findById("PCWeb").orElseThrow();

        Assert.assertEquals(2, client.getGrantTypes().size());
        Assert.assertEquals(1, client.getScopes().size());
    }
}
