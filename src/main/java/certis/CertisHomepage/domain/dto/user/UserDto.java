package certis.CertisHomepage.domain.dto.user;

import certis.CertisHomepage.domain.entity.UserEntity;
import certis.CertisHomepage.domain.dto.post.PostDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserDto {

    private Long id;
    private String account;
    private String username;
    private String nickname;
    private String email;
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;
    private Long exp;
    private Long level;
    private String roleType;
    private String status;
    private List<PostDto> posts;


    public static UserDto convertToDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .account(user.getAccount())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .registeredAt(user.getRegisteredAt())
                .modifiedAt(user.getModifiedAt())
                .exp(user.getExp())
                .level(user.getLevel())
                .roleType(user.getRoleType().toString())  // Enum to String
                .status(user.getStatus().toString())  // Enum to String
                .posts(user.getPosts().stream()
                        .map(PostDto::toDto)
                        .collect(Collectors.toList()))
                .build();

    }
}
