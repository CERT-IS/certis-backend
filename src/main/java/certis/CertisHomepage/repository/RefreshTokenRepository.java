package certis.CertisHomepage.repository;

import certis.CertisHomepage.domain.RefreshTokenEntity;
import certis.CertisHomepage.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // 토큰 문자열로 RefreshToken 엔티티 조회
    Optional<RefreshTokenEntity> findByToken(String token);

    // 사용자로 RefreshToken 엔티티 조회
    Optional<RefreshTokenEntity> findByUser(UserEntity user);

    // 사용자로 RefreshToken 엔티티 삭제
    void deleteByUser(UserEntity user);

}
