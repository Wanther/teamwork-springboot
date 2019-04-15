package teamwork.sys.controllers.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import teamwork.sys.controllers.requests.UserRequests;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserRequests.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequests form = (UserRequests) target;

        
    }
}
