package certis.CertisHomepage.common.interceptor;

import certis.CertisHomepage.common.error.ErrorCode;
import certis.CertisHomepage.common.error.TokenErrorCode;
import certis.CertisHomepage.domain.token.TokenBusiness;
import certis.CertisHomepage.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization Interceptor Url : {}", request.getRequestURI());

        // WEB, chrome 의 경우 GET, POST, OPTIONS = pass
        if (HttpMethod.OPTIONS.matches(request.getMethod())){
            return true;
        }

        //js,html,png resource를 요청하는 경우 pass
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        //TODO header 검증 jwt
        var accessToken = request.getHeader("authorization-token"); //헤더에서꺼냄
        if(accessToken == null){
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }


        var userId = tokenBusiness.validationAccessToken(accessToken);

        //인증 성공
        if(userId != null){
            var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
    }
}