package pl.jacekhorabik.urlshortener.shortenurl;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface UrlRepository extends JpaRepository<UrlEntity, String> {

  Optional<UrlEntity> findUrlEntityByHash(String hash);

  List<UrlEntity> findUrlEntityByUserId(String userId);
}
