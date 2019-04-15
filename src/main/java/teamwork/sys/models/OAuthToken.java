package teamwork.sys.models;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import teamwork.common.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "oauth_token")
public class OAuthToken extends BaseEntity<OAuthToken.PK> {

    public static OAuthToken of(String clientId, long userId, String tokenValue, LocalDateTime expireAt, String refresh, LocalDateTime refreshExpireAt) {
        OAuthToken token = new OAuthToken();
        token.setId(new OAuthToken.PK(clientId, userId));
        if (tokenValue == null) {
            tokenValue = generateTokenValue(clientId, userId);
        }
        token.setToken(tokenValue);
        token.setExpireAt(expireAt);
        if (refresh == null) {
            refresh = generateRefreshValue(clientId, userId);
        }
        token.setRefresh(refresh);
        token.setRefreshExpireAt(refreshExpireAt);
        return token;
    }

    private static String generateTokenValue(String clientId, Long userId) {
        return DigestUtils.md5Hex(clientId + userId + LocalDateTime.now().withNano(0).toInstant(ZoneOffset.ofHours(8)).getEpochSecond());
    }

    public static String generateRefreshValue(String clientId, Long userId) {
        return DigestUtils.md5Hex("refresh" + clientId + userId + LocalDateTime.now().withNano(0).toInstant(ZoneOffset.ofHours(8)).getEpochSecond());
    }

    @EmbeddedId
    private PK id;

    private String token;
    private LocalDateTime expireAt;
    private String refresh;
    private LocalDateTime refreshExpireAt;

    public OAuthToken() {}

    @Override
    public PK getId() {
        return id;
    }

    @Override
    public void setId(PK id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expirAt) {
        this.expireAt = expirAt;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public LocalDateTime getRefreshExpireAt() {
        return refreshExpireAt;
    }

    public void setRefreshExpireAt(LocalDateTime refreshExpireAt) {
        this.refreshExpireAt = refreshExpireAt;
    }

    public boolean isExpired() {
        return getExpireAt().compareTo(LocalDateTime.now()) < 0;
    }

    public OAuth2AccessToken toSpringOAuthToken() {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(getToken());
        token.setExpiration(Date.from(getExpireAt().atZone(ZoneId.systemDefault()).toInstant()));
        token.setRefreshToken(new DefaultExpiringOAuth2RefreshToken(getRefresh(), Date.from(getRefreshExpireAt().atZone(ZoneId.systemDefault()).toInstant())));
        return token;
    }

    public static class PK implements Serializable {
        private String clientId;
        private Long userId;

        public PK() {}
        public PK(String clientId, Long userId) {
            this.clientId = clientId;
            this.userId = userId;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PK pk = (PK) o;
            return Objects.equals(clientId, pk.clientId) &&
                    Objects.equals(userId, pk.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clientId, userId);
        }
    }
}
