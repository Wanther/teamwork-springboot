package teamwork.sys.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testCreate() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                .with(new AppRequestPostProcessor())
                .param("username", "wanghe")
                .param("person_id", "0")
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testQuery() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .with(new AppRequestPostProcessor())
                .param("username", "f*")
                .param("sort[username]", "asc")
                .param("page", "1")
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testUpdate() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/update")
                .with(new AppRequestPostProcessor())
                .param("id", "9")
                .param("password", "yaocong")
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/delete")
                .with(new AppRequestPostProcessor())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"id\":[\"10\",\"11\"]}")
//                .param("id", "1")
//                .param("id", "2")
        ).andDo(result -> {
            MockHttpServletResponse resp = result.getResponse();
            System.out.println(resp.getStatus());
            System.err.println(resp.getErrorMessage());
            System.out.println(resp.getContentAsString());
        });
    }

    public static class AppRequestPostProcessor implements RequestPostProcessor {

        @Override
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            request.addHeader("Authorization", "Bearer 381e1b20353a4a9a468c67cfb20dec30");
            return request;
        }
    }
}
