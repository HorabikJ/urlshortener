package pl.jacekhorabik.urlshortener.shortenurl;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;

@Entity
@Table(name = "urls")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UrlEntity {

  public UrlEntity(final String hash, final String url) {
    this.hash = hash;
    this.url = url;
  }

  @Id
  // todo add hibernate validation
  private String hash;

  private String url;

  @Generated private Instant createdAt;

  @Generated private Instant updatedAt;
}
