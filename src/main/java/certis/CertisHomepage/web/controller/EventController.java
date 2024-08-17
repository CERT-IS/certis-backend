package certis.CertisHomepage.web.controller;

import certis.CertisHomepage.service.EventService;
import certis.CertisHomepage.web.dto.Response;
import certis.CertisHomepage.web.dto.event.EventDto;
import certis.CertisHomepage.web.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController("/event")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEvents(
            @RequestParam LocalDateTime startAt,
            @RequestParam LocalDateTime endAt
    ){

        return eventService.getEvents(startAt, endAt);
    }

    //일정 작성 부분
    @PostMapping("/write")
    public Response createEvent(
            @RequestBody EventDto eventDto
    ){
        return new Response("성공", "이벤트 생성", eventService.createEvent(eventDto));
    }

    @DeleteMapping("/delete/{id}")
    public Response deleteEvent(
            @PathVariable Long id
    ){
        eventService.delete(id);
        return new Response<>("성공", "글 삭제 성공", null);
    }

}
