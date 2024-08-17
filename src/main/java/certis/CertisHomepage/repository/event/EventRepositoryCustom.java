package certis.CertisHomepage.repository.event;

import certis.CertisHomepage.domain.EventEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {

    List<EventEntity> getEvents(LocalDateTime start, LocalDateTime end);
}
