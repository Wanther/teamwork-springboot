package teamwork.sys.repos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import teamwork.sys.models.OAuthToken;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class OAuthTokenRepoTests {

    @Autowired
    private OAuthTokenRepository tokenRepo;

    @Test
    public void testGet() {
        OAuthToken token = tokenRepo.findById(new OAuthToken.PK("PCWeb", 6L)).orElseThrow();

        Assert.assertEquals("293fb7a474373a3d4b99f3a2a668ba5e", token.getRefresh());
    }

    @Test
    public void testCreateAndDelete() {
        OAuthToken token = new OAuthToken();
        OAuthToken.PK pk = new OAuthToken.PK("PCWeb", 4L);
        token.setId(pk);
        token.setToken("dadadad");
        token.setRefresh("adadadad");
        token.setExpireAt(LocalDateTime.now().plusDays(30));
        token.setRefreshExpireAt(token.getExpireAt().plusDays(30));

        token = tokenRepo.save(token);

        OAuthToken newToken = tokenRepo.findById(pk).orElseThrow();

        Assert.assertEquals(newToken.getToken(), token.getToken());

        tokenRepo.deleteById(pk);

    }

    @Test
    public void testUpdate() {
        tokenRepo.findById(new OAuthToken.PK("PCWeb", 2L)).ifPresent(oAuthToken ->{
            oAuthToken.setExpireAt(LocalDateTime.now());
            tokenRepo.save(oAuthToken);
        });
    }
}
