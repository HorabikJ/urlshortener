package pl.jacekhorabik.urlshortener.mainpage;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @Column(nullable = false)
  private String url;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  @PrePersist
  private void prePersist() {
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  private void preUpdate() {
    updatedAt = Instant.now();
  }
}
