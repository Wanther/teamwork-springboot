package teamwork.sys.auth.oauth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthTokenRepository extends CrudRepository<OAuthToken, OAuthToken.PK> {

    Optional<OAuthToken> findByToken(String token);

    Optional<OAuthToken> findByRefresh(String refresh);

    void deleteByToken(String tokenValue);

}
