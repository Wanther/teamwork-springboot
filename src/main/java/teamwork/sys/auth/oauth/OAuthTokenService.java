package teamwork.sys.auth.oauth;

import teamwork.sys.auth.user.User;

import java.util.Optional;

public interface OAuthTokenService {
    OAuthToken tokenGrantByPassword(String clientId, String clientSecret, String username, String password);

    OAuthToken tokenGrantByRefreshToken(String clientId, String clientSecret, String refreshTokenValue);

    // OAuthToken findTokenByValue(String token);

    Optional<User> findUserByToken(String tokenValue);

    void deleteToken(String tokenValue);
}
