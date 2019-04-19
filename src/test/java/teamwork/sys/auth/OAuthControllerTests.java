package teamwork.sys.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import teamwork.common.OAuthTokenPostProcessor;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class OAuthControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper jsonMapper;

    @Test
    public void testGetToken() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", "PCWeb");
        body.put("client_secret", "aA111111");
        body.put("grant_type", "password");
        body.put("username", "admin");
        body.put("password", "admin");

        String content = jsonMapper.writeValueAsString(body);
        System.out.println(content);

        mvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testRefreshToken() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", "PCWeb");
        body.put("client_secret", "aA111111");
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", "65fcbb4f847e6dc9983e4ba7cb623a69");

        String content = jsonMapper.writeValueAsString(body);
        System.out.println(content);

        mvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testGetResource() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/sys/resources")
                .accept("application/json")
                .header("Authorization", "Bearer bff91e80eb123ccb4b85bc38315219c6")
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testLogout() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/oauth/logout")
                .accept("application/json")
                .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
                .with(new OAuthTokenPostProcessor("7a0df287cd7abb134ccf811efc47857f"))
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }
}
