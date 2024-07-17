package certis.CertisHomepage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    @JsonIgnore //데이터의 이동에서, 민감정보(패스워드)를 숨기기 위해서 사용했습니다 이 어노테이션을 걸면 컨트롤러에서 리턴을 해줄 때, 걸린 부분만 빼고 보내줍니다.
    @Column(nullable = false)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registeredAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

   private Long exp;

   private Long level;


   @Column(columnDefinition = "ENUM('ADMIN', 'USER')")
   @Enumerated(EnumType.STRING) //enum에있는 이름을 매핑할때 쓰인다.
   private RoleType roleType;


    @Column(columnDefinition = "ENUM('REGISTERED', 'UNREGISTERED')")
   @Enumerated(EnumType.STRING)
   private UserStatus status;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostEntity> post;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;


}

