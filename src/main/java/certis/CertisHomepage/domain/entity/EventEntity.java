package certis.CertisHomepage.domain.entity;


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


    //date를 그냥 문자열로 받을껀가?
    private String date;

    private String eventName;

    private String host;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime postedAt;

    private LocalDateTime modifiedAt;

    private Long userId;


}
