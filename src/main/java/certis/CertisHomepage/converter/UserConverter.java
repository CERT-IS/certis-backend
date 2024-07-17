package certis.CertisHomepage.converter;

import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.web.dto.user.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserConverter {


    public UserResponse toRepsonse(UserEntity entity) {
        return UserResponse.builder()
                .account(entity.getAccount())
                .status(entity.getStatus())
                .exp(entity.getExp())
                .level(entity.getLevel())
                .name(entity.getUsername())
                .nickname(entity.getNickname())
                .build();
    }
}
