package certis.CertisHomepage.repository.event;


import certis.CertisHomepage.domain.entity.EventEntity;
import certis.CertisHomepage.domain.entity.QEventEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class EventRepositoryImpl implements EventRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EventEntity> getEvents(LocalDate start, LocalDate end) {

        QEventEntity event = QEventEntity.eventEntity;

        return queryFactory.selectFrom(event)
                .where(event.startDate.goe(start).and(event.endDate.loe(end)))
                .fetch();

    }

}
