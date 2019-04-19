package teamwork.sys.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import teamwork.common.ServiceException;
import teamwork.sys.auth.AuthService;
import teamwork.sys.auth.oauth.OAuthToken;

@RestController
class OAuthController {

    @Autowired
    private AuthService authService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new OAuthValidator());
    }

    @PostMapping(value = "/oauth/token")
    public OAuthToken login(@Validated @RequestBody OAuthRequests.TokenGrant req, BindingResult errors) throws BindException {
        if (errors.hasErrors()) {
            throw new BindException(errors);
        }

        if ("password".equals(req.getGrant_type())) {
            return authService.tokenGrantByPassword(req.getClient_id(), req.getClient_secret(), req.getUsername(), req.getPassword());
        } else if ("refresh_token".equals(req.getGrant_type())) {
            return authService.tokenGrantByRefreshToken(req.getClient_id(), req.getClient_secret(), req.getRefresh_token());
        }

        throw new ServiceException("notSupport.grantType");
    }
}
