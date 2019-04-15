package teamwork.sys.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import teamwork.common.AttributeConverters;
import teamwork.common.BaseEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "oauth_client")
public class OAuthClient extends BaseEntity<String> implements ClientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String secret;

    @Column(name = "scope")
    @Convert(converter = AttributeConverters.CommaStringToSetConverter.class)
    private Set<String> scopes;
    @Convert(converter = AttributeConverters.CommaStringToSetConverter.class)
    private Set<String> grantTypes;
    private Integer tokenValidity;
    private Integer refreshValidity;
    private String descTxt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Set<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(Set<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public Integer getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(Integer tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public Integer getRefreshValidity() {
        return refreshValidity;
    }

    public void setRefreshValidity(Integer refreshValidity) {
        this.refreshValidity = refreshValidity;
    }

    public String getDescTxt() {
        return descTxt;
    }

    public void setDescTxt(String descTxt) {
        this.descTxt = descTxt;
    }

    @Override
    public String getClientId() {
        return getId();
    }

    @Override
    public Set<String> getResourceIds() {
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return getSecret();
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return getScopes();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return getGrantTypes();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return getTokenValidity();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return getRefreshValidity();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
