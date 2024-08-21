package certis.CertisHomepage.config;

import certis.CertisHomepage.common.interceptor.AuthorizationInterceptor;
import certis.CertisHomepage.common.interceptor.NotiInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final NotiInterceptor notiInterceptor;


    private final List<String> DEFAULT_EXCLUDE = Arrays.asList(
            "/",
            "/error",
            "favicon.ico"
    );

    private final List<String> SWAGGER = Arrays.asList(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api/docs/**",
            "/**"
    );



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photo/**")
                .addResourceLocations("file:photo/");
    }

    //어떤것들을 검증하지않겠다라는걸 추가해줘야하는데.. 일일히 추가해야하므로 많아질수잇음
    //그러므로 위처럼 규칙을 만들어서 배제시킬수잇따. 근데 왜 swagger까지 검증하게뜨는거지(차단되는거지)??
    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        /*registry.addInterceptor(authorizationInterceptor)
                .excludePathPatterns(DEFAULT_EXCLUDE)
                .excludePathPatterns(SWAGGER);

        //공지사항 게시판 - 관리자만 작성 가능하게
        registry.addInterceptor(notiInterceptor)
                .addPathPatterns("/noti/*")
                .excludePathPatterns("/noti/{id}")
                .excludePathPatterns("/noti/all");
*/

    }
}
