package certis.CertisHomepage.repository;

import certis.CertisHomepage.domain.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {


    List<ImageEntity> findByPostId(Long postId);

}
