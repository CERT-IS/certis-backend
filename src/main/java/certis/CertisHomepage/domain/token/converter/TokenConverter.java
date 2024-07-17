package certis.CertisHomepage.domain.token.converter;

import certis.CertisHomepage.common.error.ErrorCode;
import certis.CertisHomepage.domain.token.controller.model.TokenResponse;
import certis.CertisHomepage.domain.token.model.TokenDto;
import certis.CertisHomepage.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TokenConverter {

    public TokenResponse toResponse(
            TokenDto accessToken,
            TokenDto refreshToken
    ){

        //널값이면 에러
        Objects.requireNonNull(accessToken, () -> {  throw new ApiException(ErrorCode.NULL_POINT);});
        Objects.requireNonNull(refreshToken, () -> {  throw new ApiException(ErrorCode.NULL_POINT);});


        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();
    }

}
