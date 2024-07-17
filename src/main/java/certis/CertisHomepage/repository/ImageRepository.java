package certis.CertisHomepage.repository;

import certis.CertisHomepage.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
