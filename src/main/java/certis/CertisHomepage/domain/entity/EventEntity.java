package certis.CertisHomepage.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime postedAt;

    private LocalDateTime modifiedAt;


    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setModifiedAt(LocalDateTime time){
        this.modifiedAt = time;
    }
}