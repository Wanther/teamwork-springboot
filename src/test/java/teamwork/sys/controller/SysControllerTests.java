package teamwork.sys.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class SysControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testUserViews() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/sys/user/views")
                .with(new UserControllerTests.AppRequestPostProcessor())
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testUserRoles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/sys/user/0/roles")
                .with(new UserControllerTests.AppRequestPostProcessor())
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }
}
