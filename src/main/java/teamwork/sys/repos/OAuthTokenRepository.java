package teamwork.sys.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.sys.models.OAuthToken;

import java.util.Optional;

@Repository
public interface OAuthTokenRepository extends CrudRepository<OAuthToken, OAuthToken.PK> {

    Optional<OAuthToken> findByToken(String token);

    Optional<OAuthToken> findByRefresh(String refresh);

    void deleteByToken(String token);

}
