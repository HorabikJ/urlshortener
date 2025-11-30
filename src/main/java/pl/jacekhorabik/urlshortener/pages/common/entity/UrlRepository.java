package pl.jacekhorabik.urlshortener.pages.common.entity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity, String> {

  Optional<UrlEntity> findUrlEntityByHash(String hash);

  List<UrlEntity> findUrlEntityByUserId(String userId);
  
  void deleteUrlEntityByHash(String hash);

  void deleteUrlEntityByUserId(String userId);

  boolean existsByHashAndUserId(String hash, String userId);

  long countUrlEntitiesByUserId(String userId);
}
