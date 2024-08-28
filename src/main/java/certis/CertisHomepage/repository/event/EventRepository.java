package certis.CertisHomepage.repository.event;

import certis.CertisHomepage.domain.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long>, EventRepositoryCustom {


}
