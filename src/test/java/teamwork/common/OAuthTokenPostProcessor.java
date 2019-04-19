package teamwork.common;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class OAuthTokenPostProcessor implements RequestPostProcessor {

    private String token;

    public OAuthTokenPostProcessor(String token) {
        this.token = token;
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }
}
