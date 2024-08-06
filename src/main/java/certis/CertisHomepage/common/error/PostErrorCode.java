package certis.CertisHomepage.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostErrorCode implements ErrorCodeIfs{

    //HttpStatusCode, 내부에서 사용할 에러코드, description

    POST_NOT_FOUND(404, 3404, "게시글을 찾을 수 없음"),
    POST_NOT_EXIST(404, 3400, "게시판을 찾을 수 없음"),
    ;

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
