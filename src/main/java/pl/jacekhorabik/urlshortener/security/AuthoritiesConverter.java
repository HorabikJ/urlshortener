package pl.jacekhorabik.urlshortener.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
class AuthoritiesConverter implements Converter<Map<String, Object>, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(final Map<String, Object> claims) {
    final Optional<Map<String, Object>> realmAccess =
        Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));
    final Optional<List<String>> roles =
        realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));
    return roles.stream()
        .flatMap(Collection::stream)
        .map(SimpleGrantedAuthority::new)
        .map(GrantedAuthority.class::cast)
        .toList();
  }
}
