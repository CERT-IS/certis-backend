package certis.CertisHomepage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean   
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 로그인 설정 추가
                .formLogin(form -> form
                        .loginPage("/login") // 로그인 페이지 경로
                        .loginProcessingUrl("/login") // 로그인 처리 경로
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 이동할 페이지
                        .failureUrl("/login") // 로그인 실패 시 이동할 페이지
                        .permitAll() // 로그인 페이지는 인증 없이 접근 허용
                )

                .authorizeHttpRequests((authz) ->
                        authz
                                .requestMatchers(
                                        "/*",
                                        "/swagger-ui/**",
                                        "/css/**", // css 경로 허용
                                        "/js/**",  // js 경로 허용
                                        "/img/**",  // img 경로 허용
                                        "/auth/*",
                                        "/common/*",
                                        "/user/login", //login 허용
                                        "/user/register"
                                ).permitAll()
                                .requestMatchers(
                                        "/project/**",
                                        "/notice/**",
                                        "/board/project/**",
                                        "/board/notice/**"
                                ).hasRole("USER")
                                .anyRequest().authenticated()
                );
        return http.build();
    }
}
