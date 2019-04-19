package teamwork.sys.compoments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import teamwork.sys.auth.AuthService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    public SecurityConfiguration() {
        super(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new OAuthTokenAuthentication.Provider(authService));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .mvcMatchers(HttpMethod.POST, "/oauth/token")
//                .and();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .servletApi().disable()
                .securityContext().disable()
                .sessionManagement().disable()
                .csrf().disable()
                .logout()
                    .logoutUrl("/oauth/logout").permitAll()
                    .addLogoutHandler(new OAuthTokenAuthentication.LogoutHandler(authService))
                    .clearAuthentication(true)
                    .deleteCookies("access_token", "refresh_token")
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).and()
                .formLogin().disable()
                .httpBasic().disable()
                .requestCache().disable()
                .anonymous().and()
                .authorizeRequests()
                    .antMatchers("/oauth/token")                    .permitAll()
                    .antMatchers("/roles", "/role/**")              .hasRole("SYS_ADMIN")
                    .antMatchers("/resources", "/resource/**")      .hasRole("SYS_ADMIN")
                    .antMatchers("/sys/lookups", "/sys/lookup/**")  .hasRole("SYS_ADMIN")
                    .antMatchers("/users", "/user/**")              .hasAnyRole("SYS_ADMIN", "ADMIN")
                    .anyRequest().authenticated().and()
                .exceptionHandling().and()
                .addFilterBefore(new OAuthTokenAuthentication.Filter(authenticationManager()), BasicAuthenticationFilter.class)
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
