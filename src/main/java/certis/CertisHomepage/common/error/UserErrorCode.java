package certis.CertisHomepage.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{

    //HttpStatusCode, 내부에서 사용할 에러코드, description

    USER_NOT_FOUND(404, 1404, "사용자를 찾을 수 없음"),
    USER_EXIST(404,1400,"사용자가 이미 있음"),
    USER_NOT_CORRET(404, 1403,"사용자가 토큰값과 맞지 않음");

    //final 붙혀서 변경되지않는값으로
    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;


    /* @Getter로 해결됨
    @Override
    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public Integer getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getDescription() {
        return this.description;
    }*/
}
