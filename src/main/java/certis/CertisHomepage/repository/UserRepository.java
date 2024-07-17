package certis.CertisHomepage.repository;

import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //query 메소드
    // select * from user where id = ? and status =? order by id desc limit 1
    //Optional<UserEntity> findFirstByIdAndStatusOrderByIdDesc(Long userId, UserStatus status);
    //Optional<UserEntity> findFirstByUserIdAndStatusOrderByIdDesc(Long userId, UserStatus status);

    // select * from user userId =? and password = ? and status = ? order by id desc limit 1
    Optional<UserEntity> findFirstByAccountAndPasswordAndStatusOrderByIdDesc(String account, String password, UserStatus status);

    Optional<UserEntity> findByAccount(String account);

    //닉네임으로 찾기?
    UserEntity findByUsername(String username);

}
