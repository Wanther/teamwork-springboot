package teamwork.sys.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamwork.App;
import teamwork.sys.models.OAuthToken;
import teamwork.sys.models.Role;
import teamwork.sys.models.User;
import teamwork.sys.repos.OAuthClientRepository;
import teamwork.sys.repos.OAuthTokenRepository;
import teamwork.sys.repos.RoleRepository;
import teamwork.sys.services.AuthService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private OAuthTokenRepository tokenRepo;
    @Autowired
    private OAuthClientRepository clientRepo;
    @Autowired
    private UserServiceImpl userDetailService;
    @Autowired
    private RoleRepository roleRepo;

    @Override
    public List<Role> findAllRolesWithResources() {
        return roleRepo.findAll();
    }

    @Cacheable("c1")
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepo.findById(clientId).orElseThrow(() -> new NoSuchClientException(clientId));
    }

    @Override
    @Transactional
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.debug("start:createAccessToken===============================");
            App.LOGGER.debug(authentication.toString());
            App.LOGGER.debug(authentication.getUserAuthentication().toString());
            App.LOGGER.debug("end:createAccessToken===============================");
        }

        User user = (User) authentication.getPrincipal();
        String clientId = authentication.getOAuth2Request().getClientId();

        ClientDetails client = loadClientByClientId(clientId);

        LocalDateTime now = LocalDateTime.now();

        OAuthToken token = OAuthToken.of(client.getClientId(), user.getId(), null, now.plusSeconds(client.getAccessTokenValiditySeconds()), null, now.plusSeconds(client.getRefreshTokenValiditySeconds()));

        // 创建token的时候，SecurityContextHolder里的Authentication是OAuthClient的，不是User的
        token.setCreatedBy(user.getId());
        token.setUpdatedBy(user.getId());

        token = tokenRepo.save(token);

        return token.toSpringOAuthToken();
    }

    @Override
    @Transactional
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.debug("start:refreshAccessToken===============================");
            App.LOGGER.debug(tokenRequest.toString());
            App.LOGGER.debug("end:refreshAccessToken===============================");
        }

        LocalDateTime now = LocalDateTime.now();

        OAuthToken token = tokenRepo.findByRefresh(refreshTokenValue).orElseThrow(() -> new InvalidGrantException("Invalid refresh token: " + refreshTokenValue));

        if (!token.getId().getClientId().equals(tokenRequest.getClientId())) {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }

        if (token.getRefreshExpireAt().compareTo(now) < 0) {
            throw new InvalidTokenException("Invalid refresh token (expired): " + refreshTokenValue);
        }

        token.setToken(OAuthToken.generateRefreshValue(token.getId().getClientId(), token.getId().getUserId()));

        ClientDetails client = loadClientByClientId(token.getId().getClientId());

        token.setExpireAt(now.plusSeconds(client.getAccessTokenValiditySeconds()));

        token.setRefreshExpireAt(now.plusSeconds(client.getRefreshTokenValiditySeconds()));

        token = tokenRepo.save(token);

        return token.toSpringOAuthToken();
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        if (App.LOGGER.isDebugEnabled()) {
            App.LOGGER.debug("start:getAccessToken===============================");
            App.LOGGER.debug(authentication.toString());
            App.LOGGER.debug(authentication.getUserAuthentication().toString());
            App.LOGGER.debug("end:getAccessToken===============================");
        }

        OAuth2Request request = authentication.getOAuth2Request();
        String clientId = request.getClientId();
        User user = (User)authentication.getUserAuthentication().getPrincipal();

        return tokenRepo.findById(new OAuthToken.PK(clientId, user.getId())).map(OAuthToken::toSpringOAuthToken).orElse(null);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuthToken token = tokenRepo.findByToken(accessToken).orElseThrow(() -> new InvalidTokenException("Invalid token (not found): " + accessToken));

        if (token.isExpired()) {
            throw new InvalidTokenException("Invalid token (expired): " + accessToken);
        }

        OAuth2Request request = new OAuth2Request(null, token.getId().getClientId(), null, true, null, null, null, null, null);

        UserDetails user = userDetailService.loadUserById(token.getId().getUserId());

        return new OAuth2Authentication(request, new PreAuthenticatedAuthenticationToken(user, null, user.getAuthorities()));
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenRepo.findByToken(accessToken).map(OAuthToken::toSpringOAuthToken).orElse(null);
    }
}
