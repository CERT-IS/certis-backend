package certis.CertisHomepage.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime postedAt;

    private LocalDateTime modifiedAt;



}
