package pl.jacekhorabik.urlshortener.userregisterlogin;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<UserEntity, String> {

  boolean existsByEmail(String email);
}
