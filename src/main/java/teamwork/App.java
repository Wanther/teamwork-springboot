package teamwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import teamwork.common.AppSortArgumentResolver;
import teamwork.common.BooleanOneZeroSerializer;
import teamwork.common.PageJacksonSerializer;

import java.util.List;

@SpringBootApplication(exclude = {TaskExecutionAutoConfiguration.class, TaskSchedulingAutoConfiguration.class,
        RestTemplateAutoConfiguration.class, MultipartAutoConfiguration.class})
@EnableTransactionManagement
@EnableCaching
public class App implements Consts {

    public static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Component
	public static class MvcConfigurer implements WebMvcConfigurer {

        @Autowired
        private PageableHandlerMethodArgumentResolverCustomizer pageCustomizer;
        @Autowired
        private SortHandlerMethodArgumentResolverCustomizer sortCustomizer;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            AppSortArgumentResolver sortResolver = new AppSortArgumentResolver();

            if (sortCustomizer != null) {
                sortCustomizer.customize(sortResolver);
            }

            resolvers.add(sortResolver);

            PageableHandlerMethodArgumentResolver pageResolver = new PageableHandlerMethodArgumentResolver(sortResolver);

            if (pageCustomizer != null) {
                pageCustomizer.customize(pageResolver);
            }

            resolvers.add(pageResolver);
        }
    }

    /*@Component
    public static class JacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {

        @Override
        public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {

            jacksonObjectMapperBuilder.serializerByType(Page.class, new PageJacksonSerializer())
                    .serializerByType(Boolean.class, new BooleanOneZeroSerializer());
        }
    }*/

}
