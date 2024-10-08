package certis.CertisHomepage.domain.dto.post;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequestDTO {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime modifiedAt;
    private List<MultipartFile> newImages;

}
