package certis.CertisHomepage.web.dto.post;


import certis.CertisHomepage.domain.PostEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {


    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private LocalDateTime registeredAt;


    //private List<MultipartFile> files;



    public static PostDto toDto(PostEntity post){
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getRegisteredAt());
    }
}
