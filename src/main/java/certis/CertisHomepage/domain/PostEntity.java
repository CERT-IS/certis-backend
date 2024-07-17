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
@ToString
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //생략 가능
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<ImageEntity> postImages = new ArrayList<>();


    public void addImage(ImageEntity image){
        this.postImages.add(image);

        if (image.getPost() != this){
            image.setPost(this);
        }
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


}
