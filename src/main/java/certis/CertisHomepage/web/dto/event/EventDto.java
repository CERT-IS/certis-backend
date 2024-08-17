package certis.CertisHomepage.web.dto.event;

import certis.CertisHomepage.domain.EventEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class EventDto {


    private String eventName;

    private String content;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


    public static EventDto toDto(EventEntity event){

        EventDto dto = EventDto.builder()
                .eventName(event.getEventName())
                .content(event.getContent())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
        return dto;
    }
}
