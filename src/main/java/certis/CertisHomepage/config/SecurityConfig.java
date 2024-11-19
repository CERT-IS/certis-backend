package certis.CertisHomepage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("account")
                        .passwordParameter("password")
                        .failureUrl("/login")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/*",
                                "/swagger-ui/**",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/auth/*",
                                "/common/*",
                                "/user/login",
                                "/user/register"
                        ).permitAll()
                        .requestMatchers(
                                "/project/**",
                                "/notice/**",
                                "/board/project/**",
                                "/board/notice/**"
                        ).hasRole("USER")
                );
        return http.build();
    }
}