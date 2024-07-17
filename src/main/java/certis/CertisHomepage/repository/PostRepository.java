package certis.CertisHomepage.repository;

import certis.CertisHomepage.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
