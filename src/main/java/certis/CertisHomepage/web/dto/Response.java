package certis.CertisHomepage.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;


//예를 들어서, data 필드가 없을 경우 (오류 처리를 할 때는, 없을 수 있음)
//data 필드는 응답에서 그냥 빼버린다는 뜻입니다.
@JsonInclude(JsonInclude.Include.NON_NULL) //  null 값을 가지는 필드는, JSON 응답에 포함되지 않음
@Getter
@AllArgsConstructor
public class Response<T> {

    //응답을 하는 부분 controller에서 어떤 형식으로 응답을할건지.

    private String success;
    private String message;
    private T data;
}
