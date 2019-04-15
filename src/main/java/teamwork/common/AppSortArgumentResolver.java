package teamwork.common;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppSortArgumentResolver extends SortHandlerMethodArgumentResolver {

    private static final char PROPERTY_START = '[';
    private static final char PROPERTY_END = ']';

    @Override
    public Sort resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        List<Sort.Order> orders = null;
        final String sortParameterPreffix = getSortParameter(parameter) + PROPERTY_START;

        Iterator<String> it = webRequest.getParameterNames();
        while (it.hasNext()) {
            final String parameterName = it.next();
            if (parameterName.startsWith(sortParameterPreffix)) {
                final String property = parameterName.substring(sortParameterPreffix.length(), parameterName.indexOf(PROPERTY_END));
                final String direction = webRequest.getParameter(parameterName);

                if (orders == null) {
                    orders = new ArrayList<>();
                }

                orders.add("desc".equalsIgnoreCase(direction) ? Sort.Order.desc(property) : Sort.Order.asc(property));

            }
        }

        if (orders == null) {
            return Sort.unsorted();
        }

        return Sort.by(orders);
    }


}
