package certis.CertisHomepage.domain.dto.post;


import certis.CertisHomepage.domain.BoardType;
import certis.CertisHomepage.domain.entity.PostEntity;
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

    private BoardType boardType;

    private Long userId;

    private Long view;

    private LocalDateTime modifiedAt;

    //private List<MultipartFile> files;



    public static PostDto toDto(PostEntity post){
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getRegisteredAt(), post.getBoardType(),post.getUser().getId(),post.getView(),post.getModifiedAt());
    }
}
