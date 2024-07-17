package certis.CertisHomepage.domain.token;

import certis.CertisHomepage.common.error.ErrorCode;
import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.domain.token.controller.model.TokenResponse;
import certis.CertisHomepage.domain.token.converter.TokenConverter;
import certis.CertisHomepage.domain.token.service.TokenService;
import certis.CertisHomepage.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenBusiness {

    private final TokenService tokenService;
    private final TokenConverter tokenConverter;

    /*
    * 1.    user entity userId 추출
    * 2.    access, refresh token 발행
    * 3.    converter -> token response로 변경
    * */

    public TokenResponse issueToken(UserEntity userEntity){

        return Optional.ofNullable(userEntity)
                .map(ue -> {
                    return ue.getId();
                })
                .map(userId -> {

                    var accessToken = tokenService.issueAccessToken(userId);
                    var refreshToken = tokenService.issueRefreshToken(userId);
                    return tokenConverter.toResponse(accessToken, refreshToken);

                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    public Long validationAccessToken(String accessToken){
        var Id = tokenService.validationToken(accessToken);
        return Id;
    }


}
