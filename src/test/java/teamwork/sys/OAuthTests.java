package teamwork.sys;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class OAuthTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper jsonMapper;

    @Test
    public void testGetToken() throws Exception {
        String jsonBody = jsonMapper.writeValueAsString(Map.of("client_id", "PCWeb", "client_secret", "aA111111", "grant_type", "password", "username", "wanghe", "password", "wanghe"));
        mvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .contentType("application/json")
                .accept("application/json")
                .content(jsonBody)
//                .param("client_id", "PCWeb")
//                .param("client_secret", "aA111111")
//                .param("grant_type", "password")
//                .param("username", "wanghe")
//                .param("password", "wanghe")
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testRefreshToken() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .accept("application/json")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("PCWeb", "aA111111"))
                .param("refresh_token", "913f5bdd62848afe9eaf0831bd76a417")
                .param("grant_type", "refresh_token")
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
}
