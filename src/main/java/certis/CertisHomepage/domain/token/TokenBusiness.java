package certis.CertisHomepage.domain.token;

import certis.CertisHomepage.common.error.ErrorCode;
import certis.CertisHomepage.common.error.TokenErrorCode;
import certis.CertisHomepage.domain.entity.RefreshTokenEntity;
import certis.CertisHomepage.domain.entity.UserEntity;
import certis.CertisHomepage.domain.token.controller.model.TokenResponse;
import certis.CertisHomepage.domain.token.converter.TokenConverter;
import certis.CertisHomepage.domain.token.model.TokenDto;
import certis.CertisHomepage.domain.token.service.TokenService;
import certis.CertisHomepage.common.exception.ApiException;
import certis.CertisHomepage.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenBusiness {

    private final TokenService tokenService;
    private final TokenConverter tokenConverter;
    private final RefreshTokenRepository refreshTokenRepository;


    /*
    * 1.    user entity userId 추출
    * 2.    access, refresh token 발행
    * 3.    converter -> token response로 변경
    * */

    @Transactional
    public TokenResponse issueToken(UserEntity userEntity){

        return Optional.ofNullable(userEntity)
                .map(UserEntity::getId)
                .map(userId -> {

                    var accessToken = tokenService.issueAccessToken(userId);
                    var refreshToken = tokenService.issueRefreshToken(userId);

                    //사용자가 가지고있던 기존 리프레쉬 토큰 삭제
                    refreshTokenRepository.findByUser(userEntity)
                                    .ifPresentOrElse(
                                            existingToken -> { //존재하는 토큰은
                                                existingToken.setToken(refreshToken.getToken());
                                                existingToken.setExpiredAt(refreshToken.getExpiredAt());
                                                refreshTokenRepository.save(existingToken);
                                            },
                                            () -> {  // 없다면
                                                RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                                                        .token(refreshToken.getToken())
                                                        .expiredAt(refreshToken.getExpiredAt())
                                                        .user(userEntity).build();
                                                refreshTokenRepository.save(refreshTokenEntity);
                                            }
                                    );


                    return tokenConverter.toResponse(accessToken, refreshToken);

                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }

    public Long validationAccessToken(String accessToken){
        var id = tokenService.validationToken(accessToken);
        return id;
    }

    //accessToken 재발급 메소드
    public TokenResponse reissueAccessToken(String refreshToken){

        //db에서 조회하고
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ApiException(TokenErrorCode.INVALID_TOKEN));

        //refreshtoken 만료됬는지 안됬는지 확인하고
        if (isRefreshTokenExpired(refreshTokenEntity.getExpiredAt())){
            throw new ApiException(TokenErrorCode.EXPIRED_TOKEN);
        }


        //있으면 그걸로 재발급
        var newAccessToken = tokenService.issueAccessToken(refreshTokenEntity.getUser().getId());


        var freshToken = refreshTokenRepository.findByToken(refreshToken).get();
        var refresh = TokenDto.builder()
                .token(freshToken.getToken())
                .expiredAt(freshToken.getExpiredAt())
                .build();

        return tokenConverter.toResponse(newAccessToken, refresh);

    }

    private boolean isRefreshTokenExpired(LocalDateTime expireDate) {
        return expireDate.isBefore(LocalDateTime.now());
    }


    //로그아웃 시 refreshToken 삭제
    public void logout(UserEntity user){
        refreshTokenRepository.deleteByUser(user);
    }

}
