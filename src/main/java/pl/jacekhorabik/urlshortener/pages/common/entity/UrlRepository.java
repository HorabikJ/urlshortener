package pl.jacekhorabik.urlshortener.pages.common.entity;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity, String> {

  Optional<UrlEntity> findUrlEntityByHash(String hash);

  Page<UrlEntity> findUrlEntityByUserId(String userId, PageRequest pageRequest);

  void deleteUrlEntityByHash(String hash);

  void deleteUrlEntityByUserId(String userId);

  boolean existsByHashAndUserId(String hash, String userId);

  long countUrlEntitiesByUserId(String userId);
}
