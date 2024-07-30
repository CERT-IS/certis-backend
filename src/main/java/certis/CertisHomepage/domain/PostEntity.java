package certis.CertisHomepage.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long view;

    private LocalDateTime registeredAt;

    private LocalDateTime modifiedAt;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) //생략 가능
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("id desc")
    @Builder.Default
    private List<ImageEntity> postImages = new ArrayList<>();


    public void addImage(ImageEntity image){
        this.postImages.add(image);

        if (image.getPost() != this){
            image.setPost(this);
        }
    }


    public Long getUserId(UserEntity user){
        return user.getId();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt){
        this.modifiedAt = modifiedAt;
    }

    public void setView(Long view) {
        this.view = view;
    }

    public void increaseViewCnt(){
        this.view++;
    }
}
