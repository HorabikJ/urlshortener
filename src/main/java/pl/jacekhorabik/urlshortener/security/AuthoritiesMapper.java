package pl.jacekhorabik.urlshortener.security;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AuthoritiesMapper implements GrantedAuthoritiesMapper {

  private final AuthoritiesConverter authoritiesConverter;

  @Override
  public Collection<? extends GrantedAuthority> mapAuthorities(
      final Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .filter(authority -> authority instanceof OidcUserAuthority)
        .map(OidcUserAuthority.class::cast)
        .map(OidcUserAuthority::getIdToken)
        .map(OidcIdToken::getClaims)
        .map(authoritiesConverter::convert)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }
}
