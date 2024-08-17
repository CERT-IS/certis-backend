package certis.CertisHomepage.service;

import certis.CertisHomepage.common.error.ErrorCode;
import certis.CertisHomepage.domain.EventEntity;
import certis.CertisHomepage.exception.ApiException;
import certis.CertisHomepage.repository.event.EventRepository;
import certis.CertisHomepage.web.dto.event.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    public List<EventDto> getEvents(LocalDateTime start, LocalDateTime end) {

        List<EventEntity> events = eventRepository.getEvents(start, end);

        return events.stream()
                .map(EventDto::toDto)
                .collect(Collectors.toList());
    }


    public EventDto createEvent(EventDto eventDto) {

        EventEntity entity = EventEntity.builder()
                .eventName(eventDto.getEventName())
                .content(eventDto.getContent())
                .startDate(eventDto.getStartDate())
                .endDate(eventDto.getEndDate())
                .postedAt(LocalDateTime.now())
                .build();

        eventRepository.save(entity);

        return EventDto.toDto(entity);

    }

    public void delete(Long id){
        if(!eventRepository.existsById(id)){
            throw new ApiException(ErrorCode.BAD_REQUEST, "이벤트가 없음");
        }
        eventRepository.deleteById(id);
    }
}
