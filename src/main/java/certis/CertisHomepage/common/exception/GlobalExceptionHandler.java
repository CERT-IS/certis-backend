package certis.CertisHomepage.common.exception;

import certis.CertisHomepage.domain.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //@ExceptionHandler 어노테이션에 해당하는 메소드 부분으로
    //들어가게됨

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> illegalArgumentExceptionAdvice(IllegalArgumentException e){
        log.error("",e.getMessage());

        return new Response("fail", e.getMessage(), null);
    }

}
