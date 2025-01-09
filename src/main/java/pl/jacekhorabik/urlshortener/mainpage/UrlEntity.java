package pl.jacekhorabik.urlshortener.mainpage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

  @CreationTimestamp
  @Column(nullable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private Instant updatedAt;
}
