package certis.CertisHomepage.domain.token.helper;

import certis.CertisHomepage.common.error.ErrorCode;
import certis.CertisHomepage.common.error.TokenErrorCode;
import certis.CertisHomepage.domain.token.ifs.TokenHelperIfs;
import certis.CertisHomepage.domain.token.model.TokenDto;
import certis.CertisHomepage.exception.ApiException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenHelper implements TokenHelperIfs {

    @Value("${token.secret.key}") //lombok말고 org.springframework.beans.factory.annotation.Value;
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;




    @Override
    public TokenDto issueAccessToken(Map<String, Object> data) {

        //먼저 만료시간을 정해야함.
        var expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);

        var expiredAt = Date.from(
                expiredLocalDateTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );

        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime).build();
    }

    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {
        var expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);

        var expiredAt = Date.from(
                expiredLocalDateTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );

        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();


        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime).build();

    }

    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try{
            var result = parser.parseClaimsJws(token);
            return new HashMap<String, Object>(result.getBody());

        }catch (Exception e){

            if(e instanceof SignatureException){
                //토큰이 유효하지 않을때
                throw new ApiException(TokenErrorCode.INVALID_TOKEN, e);
            }

            else if (e instanceof ExpiredJwtException){
                //만료된 토큰일때
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN, e);

            }
            else {
                //알수없는 그 외 에러
                throw new ApiException(TokenErrorCode.TOKEN_EXCEPTION,e);
            }
        }

    }
}
