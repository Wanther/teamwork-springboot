package teamwork.sys.auth;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static String token;

    @Before
    public void login() throws Exception {
        JSONObject params = new JSONObject();
        params.put("client_id", "PCWeb");
        params.put("client_secret", "aA111111");
        params.put("grant_type", "password");
        params.put("username", "admin");
        params.put("password", "admin");

        mvc.perform(post("/oauth/token")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(params.toString())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andDo(result -> token = new JSONObject(result.getResponse().getContentAsString()).getString("access_token"));
    }

    @Test
    public void test001Query() throws Exception {

        long rowCount = countRowsInTable(jdbcTemplate, "sys_resource");

        mvc.perform(get("/sys/resources")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(rowCount))
                .andExpect(jsonPath("$.data[0].id").isNotEmpty())
                .andExpect(jsonPath("$.data[0].type").isNumber())
                .andExpect(jsonPath("$.data[0].desc_txt").isNotEmpty());
    }

    protected String getBearerToken() {
        return "Bearer " + token;
    }
}
