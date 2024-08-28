package certis.CertisHomepage.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "image")
public class ImageEntity {
    //ImageEntity라고 이름은 지었지만 그냥 모든 유형의 파일을 다 받을생각임.

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
