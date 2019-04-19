package teamwork.common;

import org.json.JSONObject;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ApiTestBase extends TestBase {
    private String token;

    @Autowired
    private MockMvc mvc;

    @Before
    public void login() throws Exception {
        JSONObject params = new JSONObject();
        params.put("client_id", "PCWeb");
        params.put("client_secret", "aA111111");
        params.put("grant_type", "password");
        params.put("username", getUsername());
        params.put("password", getPassword());

        mvc.perform(post("/oauth/token")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(params.toString())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andDo(result -> token = new JSONObject(result.getResponse().getContentAsString()).getString("access_token"));
    }

    protected MockMvc getMvc() {
        return mvc;
    }

    protected String getBearerToken() {
        return "Bearer " + token;
    }

    protected String getUsername() {
        return "admin";
    }

    protected String getPassword() {
        return "admin";
    }
}
