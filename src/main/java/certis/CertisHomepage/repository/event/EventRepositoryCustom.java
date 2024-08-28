package certis.CertisHomepage.repository.event;

import certis.CertisHomepage.domain.entity.EventEntity;

import java.time.LocalDate;
import java.util.List;

public interface EventRepositoryCustom {

    List<EventEntity> getEvents(LocalDate start, LocalDate end);
}
