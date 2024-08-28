package certis.CertisHomepage.controller;

import certis.CertisHomepage.service.EventService;
import certis.CertisHomepage.domain.dto.Response;
import certis.CertisHomepage.domain.dto.event.EventDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEvents(
            @RequestParam LocalDate startAt,
            @RequestParam LocalDate endAt
    ){

        return eventService.getEvents(startAt, endAt);
    }

    //일정 작성 부분
    @PostMapping("/write")
    public Response createEvent(
            @Valid
            @RequestBody EventDto eventDto
    ){
        return new Response("성공", "이벤트 생성", eventService.createEvent(eventDto));
    }

    @Operation(description = "이벤트 수정")
    @PostMapping("/{id}")
    public Response updateEvent(
            @PathVariable Long id,
            @Valid
            @RequestBody EventDto eventDto
    ){
        return new Response("성공", "이벤트 수정 ", eventService.updateEvent(id, eventDto));
    }

    @DeleteMapping("/delete/{id}")
    public Response deleteEvent(
            @PathVariable Long id
    ){
        eventService.delete(id);
        return new Response<>("성공", "글 삭제 성공", null);
    }

}
