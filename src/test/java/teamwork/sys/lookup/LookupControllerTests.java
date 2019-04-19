package teamwork.sys.lookup;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.StringUtils;
import teamwork.common.OAuthTokenPostProcessor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTableWhere;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LookupControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static String token;
    private static List<Long> lookupIds = new LinkedList<>();

    private String getBearerToken() {
        return "Bearer " + token;
    }

    @Before
    public void login() throws Exception {

        if (!StringUtils.isEmpty(token)) {
            return;
        }

        System.out.println("login===============================================");

        deleteFromTableWhere(jdbcTemplate, "sys_lookup", "type=?", "test");

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
    public void test001Create() throws Exception {

        List<JSONObject> lookups = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            JSONObject json = new JSONObject();
            json.put("type", "test");
            json.put("value", String.valueOf(i));
            json.put("text", "test" + i);

            lookups.add(json);
        }

        for (JSONObject lookup : lookups) {
            mvc.perform(post("/sys/lookup/add")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(lookup.toString())
            )
                    .andExpect(status().isOk());
        }

    }

    @Test
    public void test002Query() throws Exception {

        mvc.perform(get("/sys/lookups")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .accept(MediaType.APPLICATION_JSON)
                .param("type", "test")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5))
                .andDo(result -> {
                    lookupIds.clear();
                    JSONObject json = new JSONObject(result.getResponse().getContentAsString());
                    JSONArray data = json.getJSONArray("data");
                    for (int i = 0, size = data.length(); i < size; i++) {
                        JSONObject lookup = data.getJSONObject(i);
                        lookupIds.add(lookup.getLong("id"));
                    }
                });

        mvc.perform(get("/sys/lookups")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .accept(MediaType.APPLICATION_JSON)
                .param("type", "test")
                .param("value", "1")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    public void test003Update() throws Exception {
        Assert.assertNotEquals(0, lookupIds.size());

        Long viewId = lookupIds.get(new Random().nextInt(lookupIds.size()));

        MvcResult result = mvc.perform(get("/sys/lookup/" + viewId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(viewId))
                .andExpect(jsonPath("$.type").value("test"))
                .andExpect(jsonPath("$.value").isNotEmpty())
                .andExpect(jsonPath("$.text").isNotEmpty())
                .andExpect(jsonPath("$.inactive").value(0))
                .andReturn();

        JSONObject lookup = new JSONObject(result.getResponse().getContentAsString());
        lookup.put("text", "updated");

        mvc.perform(post("/sys/lookup/edit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .content(lookup.toString())
        ).andExpect(status().isOk());

    }

    @Test
    public void test004Delete() throws Exception {
        Assert.assertNotEquals(0, lookupIds.size());

        JSONObject json = new JSONObject();
        JSONArray ids = new JSONArray();
        lookupIds.forEach(ids::put);

        json.put("id", ids);

        mvc.perform(post("/sys/lookup/delete")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .content(json.toString())
        ).andExpect(status().isOk());
    }

    public RequestPostProcessor oauth() {
        return new OAuthTokenPostProcessor(token);
    }
}
