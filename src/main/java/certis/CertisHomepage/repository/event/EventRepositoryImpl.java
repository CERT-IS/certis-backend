package certis.CertisHomepage.repository.event;


import certis.CertisHomepage.domain.EventEntity;
import certis.CertisHomepage.domain.QEventEntity;
import certis.CertisHomepage.web.dto.event.EventDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class EventRepositoryImpl implements EventRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EventEntity> getEvents(LocalDateTime start, LocalDateTime end) {

        QEventEntity event = QEventEntity.eventEntity;

        return queryFactory.selectFrom(event)
                .where(event.startDate.between(start, end).and(event.endDate.between(start, end)))
                .fetch();

    }

}
