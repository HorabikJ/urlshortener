package pl.jacekhorabik.urlshortener.userregisterlogin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.jacekhorabik.urlshortener.common.entity.BaseEntity;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserEntity extends BaseEntity {

  @Id private String id;
  private String email;
  private String password;

  UserEntity(final String email, final String password) {
    //  todo maybe some custom hibernate generator for this?
    this.id = UUID.randomUUID().toString();
    this.email = email;
    this.password = password;
  }
}
