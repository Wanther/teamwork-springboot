package teamwork.sys.auth.web;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import teamwork.sys.auth.web.OAuthRequests;

import java.util.HashSet;
import java.util.Set;

public class OAuthValidator implements Validator {

    private static final Set<String> GRANT_TYPES = new HashSet<>();

    static {
        GRANT_TYPES.add("password");
        GRANT_TYPES.add("refresh_token");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(OAuthRequests.TokenGrant.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final OAuthRequests.TokenGrant tokenGrant = (OAuthRequests.TokenGrant) target;
        if (!GRANT_TYPES.contains(tokenGrant.getGrant_type())) {
            errors.reject("grant_type", "notSupported");
            return;
        }

        switch (tokenGrant.getGrant_type()) {
            case "password":
                if (StringUtils.isEmpty(tokenGrant.getUsername())) {
                    errors.rejectValue("username", "required");
                }
                if (StringUtils.isEmpty(tokenGrant.getPassword())) {
                    errors.rejectValue("password", "required");
                }
                break;
            case "refresh_token":
                if (StringUtils.isEmpty(tokenGrant.getRefresh_token())) {
                    errors.reject("refresh_token", "required");
                }
                break;
        }
    }
}
