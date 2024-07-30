package certis.CertisHomepage.web.dto.post;

import certis.CertisHomepage.domain.PostEntity;
import certis.CertisHomepage.domain.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GetPostResponse {

    private Long id;

    private String title;

    private String content;

    private String writer;

    //이미지파일이 db에 이런 'photo\\20240719\\168247492163800.jpg' 형식으로 저장되어있으니 getImage메소드로 불러오는 부분을 따로만든다.
    //그러면 postService에서 findByPostId로 이미지의 img_url을 알아내서 for문으로 밑의 변수에 넣고 그거를 보여주는 방법으로 해보자.
    private List<String> PostImageUrlList;

    private LocalDateTime registeredAt;


    public static GetPostResponse toDto(PostEntity post, List<String> imageUrls){

        GetPostResponse responsedto = GetPostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .PostImageUrlList(imageUrls)
                .title(post.getTitle())
                .writer(post.getUser().getUsername())
                .registeredAt(post.getRegisteredAt())
                .build()
                ;
        return responsedto;
    }


}
