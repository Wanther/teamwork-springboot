package teamwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import teamwork.common.AppSortArgumentResolver;
import teamwork.common.PageJacksonSerializer;
import teamwork.sys.services.AuthService;
import teamwork.sys.services.impl.UserServiceImpl;

import java.util.List;

@SpringBootApplication(exclude = {TaskExecutionAutoConfiguration.class, TaskSchedulingAutoConfiguration.class,
        RestTemplateAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
@EnableTransactionManagement
@EnableAuthorizationServer
@EnableResourceServer
@EnableCaching
public class App {

    public static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @Component
	public class MvcConfigurer implements WebMvcConfigurer {

        @Autowired
        private SpringDataWebProperties properties;
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

    @Component
    public class JacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {

        @Override
        public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
            jacksonObjectMapperBuilder.serializerByType(Page.class, new PageJacksonSerializer());
        }
    }

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

	@Component
	public static class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

        private AuthenticationManager authenticationManager;
	    @Autowired
	    private AuthService authService;
	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    public AuthorizationServerConfigurer(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            authenticationManager = authenticationConfiguration.getAuthenticationManager();
        }

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(authService);
		}

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.authenticationManager(authenticationManager)
                    .tokenServices(authService);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) {
            security.passwordEncoder(passwordEncoder)
                    .allowFormAuthenticationForClients();
        }
    }

    @Component
    public static class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

	    @Autowired
	    private UserServiceImpl userDetailService;
        @Autowired
        private AuthService authService;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.userDetailsService(userDetailService)
                    .authorizeRequests()
                        .antMatchers("/users", "/user/**").hasAnyRole("SYS_ADMIN", "ADMIN")
                        .anyRequest().authenticated();
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.stateless(true)
                    .tokenServices(authService);
        }

    }

}
