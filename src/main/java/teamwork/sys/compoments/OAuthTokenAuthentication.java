package teamwork.sys.compoments;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import teamwork.sys.auth.oauth.OAuthTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class OAuthTokenAuthentication extends AbstractAuthenticationToken {

    private static Optional<String> extractToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            final Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                token = Arrays.stream(cookies).filter(cookie -> "access_token".equals(cookie.getName())).map(Cookie::getValue).findFirst().orElse(null);
            }

        }

        if (StringUtils.isEmpty(token)) {
            return Optional.empty();
        }

        if (token.matches("^Bearer [a-zA-Z0-9-._~+/]+$")) {
            return Optional.of(token.substring(7));
        }

        return Optional.of(token);
    }

    private String token;

    private OAuthTokenAuthentication(String token) {
        super(null);
        Assert.notNull(token, "token must not be null");
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    public static class Filter extends OncePerRequestFilter {

        private final AuthenticationManager authenticationManager;

        public Filter(AuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            Optional<String> token = extractToken(request);

            if (token.isPresent()) {
                try {
                    Authentication authentication = authenticationManager.authenticate(new OAuthTokenAuthentication(token.get()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (AuthenticationException e) {
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }

            filterChain.doFilter(request, response);

        }


    }

    public static class Provider implements AuthenticationProvider {

        private final OAuthTokenService tokenService;

        public Provider(OAuthTokenService tokenService) {
            this.tokenService = tokenService;
        }

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            OAuthTokenAuthentication tokenAuthentication = (OAuthTokenAuthentication) authentication;

            return tokenService.findUserByToken((String) tokenAuthentication.getCredentials())
                    .map(user -> new PreAuthenticatedAuthenticationToken(user, user.getUsername(), user.getAuthorities()))
                    .orElseThrow(() -> new BadCredentialsException("invalid.token"));
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.isAssignableFrom(OAuthTokenAuthentication.class);
        }

    }

    public static class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {
        private final OAuthTokenService tokenService;

        public LogoutHandler(OAuthTokenService tokenService) {
            this.tokenService = tokenService;
        }

        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            OAuthTokenAuthentication.extractToken(request).ifPresent(tokenService::deleteToken);
        }
    }
}
