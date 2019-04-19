package teamwork.sys.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import teamwork.common.ServiceException;
import teamwork.sys.auth.oauth.OAuthClient;
import teamwork.sys.auth.oauth.OAuthToken;
import teamwork.sys.auth.role.Role;
import teamwork.sys.auth.user.User;
import teamwork.sys.auth.oauth.OAuthClientRepository;
import teamwork.sys.auth.oauth.OAuthTokenRepository;
import teamwork.sys.auth.role.RoleRepository;
import teamwork.sys.auth.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private OAuthTokenRepository tokenRepo;
    @Autowired
    private OAuthClientRepository clientRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<Role> findAllRolesWithResources() {
        return roleRepo.findAll();
    }

    @Transactional
    @Override
    public OAuthToken tokenGrantByPassword(String clientId, String clientSecret, String username, String password) {

        OAuthClient client = clientRepo.findById(clientId).orElseThrow(() -> new ServiceException("notExist.client"));

        if (!passwordEncoder.matches(clientSecret, client.getSecret())) {
            throw new ServiceException("invalid.secret");
        }

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ServiceException("notExist.user"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException("invalid.password");
        }

        LocalDateTime now = LocalDateTime.now();

        OAuthToken token = tokenRepo.findById(new OAuthToken.PK(client.getId(), user.getId()))
                .orElse(OAuthToken.of(client.getId(), user.getId()));

        token.setToken(OAuthToken.generateTokenValue(client.getId(), user.getId()));
        token.setRefresh(OAuthToken.generateRefreshValue(client.getId(), user.getId()));
        token.setExpireAt(now.plusSeconds(client.getTokenValidity()));
        token.setRefreshExpireAt(now.plusSeconds(client.getRefreshValidity()));
        // 创建token的时候，SecurityContextHolder里的Authentication是空的
        token.setCreatedBy(user.getId());
        token.setUpdatedBy(user.getId());
        token.setCreated(now);
        token.setUpdated(now);

        return tokenRepo.save(token);
    }

    @Override
    public OAuthToken tokenGrantByRefreshToken(String clientId, String clientSecret, String refreshTokenValue) {

        OAuthClient client = clientRepo.findById(clientId).orElseThrow(() -> new ServiceException("notExist.client"));

        if (!passwordEncoder.matches(clientSecret, client.getSecret())) {
            throw new ServiceException("invalid.secret");
        }

        LocalDateTime now = LocalDateTime.now();

        OAuthToken token = tokenRepo.findByRefresh(refreshTokenValue).orElseThrow(() -> new ServiceException("notExist.token"));

        if (!token.getId().getClientId().equals(clientId)) {
            throw new ServiceException("notMatch.client");
        }

        if (token.getRefreshExpireAt().compareTo(now) < 0) {
            throw new ServiceException("expired.token");
        }

        token.setToken(OAuthToken.generateRefreshValue(token.getId().getClientId(), token.getId().getUserId()));

        token.setExpireAt(now.plusSeconds(client.getTokenValidity()));

        token.setRefreshExpireAt(now.plusSeconds(client.getRefreshValidity()));

        token.setUpdatedBy(token.getId().getUserId());
        token.setUpdated(now);

        return tokenRepo.save(token);
    }

    @Override
    public Optional<User> findUserByToken(String token) {
        return tokenRepo.findByToken(token)
                .map(oAuthToken -> userRepo.findRolesById(oAuthToken.getId().getUserId()).orElse(null));
    }

    @Override
    @Transactional
    public void deleteToken(String tokenValue) {
        tokenRepo.deleteByToken(tokenValue);
    }
}
