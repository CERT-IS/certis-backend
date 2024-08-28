package certis.CertisHomepage.converter;

import certis.CertisHomepage.domain.entity.UserEntity;
import certis.CertisHomepage.domain.dto.user.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserConverter {


    public UserResponse toResponse(UserEntity entity) {
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
