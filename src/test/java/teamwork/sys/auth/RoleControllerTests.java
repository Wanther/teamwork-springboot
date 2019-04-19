package teamwork.sys.auth;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import teamwork.common.ApiTestBase;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RoleControllerTests extends ApiTestBase {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test001Query() throws Exception {

        long rowCount = countRowsInTable(jdbcTemplate, "sys_role");

        mvc.perform(get("/sys/roles")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(rowCount))
                .andExpect(jsonPath("$.data[0].id").isNotEmpty())
                .andExpect(jsonPath("$.data[0].desc_txt").isNotEmpty());
    }
}
