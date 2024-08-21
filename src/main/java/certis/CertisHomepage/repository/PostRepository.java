package certis.CertisHomepage.repository;

import certis.CertisHomepage.domain.BoardType;
import certis.CertisHomepage.domain.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findByBoardType(BoardType boardType, Pageable pageable);

}
