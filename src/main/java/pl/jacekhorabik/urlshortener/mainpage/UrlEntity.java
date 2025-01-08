package pl.jacekhorabik.urlshortener.mainpage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "urls")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UrlEntity {

  public UrlEntity(String hash, String url) {
    this.hash = hash;
    this.url = url;
  }

  @Id
  // todo add hibernate validation
  private String hash;

  @Column(updatable = false, nullable = false)
  private String url;
  
  @CreationTimestamp
  @Column(updatable = false, nullable = false)
  private Instant createdAt;
  
}
