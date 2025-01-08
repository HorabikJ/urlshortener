package pl.jacekhorabik.urlshortener.mainpage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UrlRepository extends JpaRepository<UrlEntity, String> {

    Optional<UrlEntity> findUrlEntityByHash(String hash);

}
