package certis.CertisHomepage.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalImgName; //파일 원본명

    private String imgUrl; //파일 저장 경로

    //private String imgName; //서버에 저장될 이미지명

    //private  Long fileSize;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private Long projectId;


    public void setPost(PostEntity post){
        this.post = post;

        if(!post.getPostImages().contains(this)){
            post.getPostImages().add(this);
        }
    }

}

