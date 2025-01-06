package pl.jacekhorabik.urlshortener.mainpage;

import org.springframework.data.jpa.repository.JpaRepository;

interface UrlRepository extends JpaRepository<UrlEntity, String> {
    
}
