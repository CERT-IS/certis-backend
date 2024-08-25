package certis.CertisHomepage.web.dto.event;

import certis.CertisHomepage.domain.EventEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class EventDto {


    @NotBlank
    private String eventName;

    @NotNull
    private String content;

    private LocalDate startDate;

    private LocalDate endDate;


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
