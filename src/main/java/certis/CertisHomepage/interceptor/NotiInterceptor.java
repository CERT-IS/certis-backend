package certis.CertisHomepage.interceptor;

import certis.CertisHomepage.domain.RoleType;
import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.domain.UserStatus;
import certis.CertisHomepage.domain.token.TokenBusiness;
import certis.CertisHomepage.domain.token.service.TokenService;
import certis.CertisHomepage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NotiInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        var accessToken = request.getHeader("authorization-token"); //헤더에서꺼냄

        //로그인 안되었을때는 로그인 화면으로 보내주고
        if(accessToken == null){
            request.setAttribute("message", " 로그인 하십시오.");
            request.setAttribute("path", "/login");

            return false;
        }else{
            //로그인 되어있을때 관리자인지 일반유저인지 확인해서 관리자는 글작성할수있게
            //TODO 일단 true리턴
            //accesstoken을 받아서 validationToken메소드로 검증하고 id받아옴
            var loginid = tokenBusiness.validationAccessToken(accessToken);
            UserEntity user = userRepository.findByIdAndStatus(loginid, UserStatus.REGISTERED);

            if(RoleType.ADMIN.equals(user.getRoleType()) ){
                //관리자면 접근 허용
                return true;
            }else {
                //일반 유저면
                request.setAttribute("message", "관리자만 접근할 수있습니다.");
                request.setAttribute("path", "/noti"); // 그냥 보드 페이지로 돌아가게
                request.getRequestDispatcher("/noti").forward(request, response);//공지사항 페이지(/noti)로 포워딩하여
                return false;
            }

        }
    }
}
