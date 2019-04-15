package teamwork.sys.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamwork.sys.models.OAuthClient;

import java.util.Optional;

@Repository
public interface OAuthClientRepository extends CrudRepository<OAuthClient, String> {

    @Override
    Optional<OAuthClient> findById(String s);
}
