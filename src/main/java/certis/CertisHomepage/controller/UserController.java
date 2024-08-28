package certis.CertisHomepage.controller;

import certis.CertisHomepage.domain.token.TokenBusiness;
import certis.CertisHomepage.domain.token.controller.model.TokenResponse;
import certis.CertisHomepage.common.exception.ApiException;
import certis.CertisHomepage.service.UserService;
import certis.CertisHomepage.domain.dto.user.UserLoginRequest;
import certis.CertisHomepage.domain.dto.user.UserRegisterRequest;
import certis.CertisHomepage.domain.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final TokenBusiness tokenBusiness;

    //swagger 에서 설명부분을 나타내는 어노테이션.
    @Operation(summary = "전체 회원 보기", description = "전체 회원을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response<?> findAll() {

        return new Response<>("ture", "조회 성공", userService.findAll());
    }


    @Operation(summary = "유저 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response<?> findUser(@PathVariable("id") Long id){
        return new Response<>("true", "조회 성공", userService.findUser(id));
    }

    @Operation(summary = "회원가입")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Response register(
            @Valid
            @RequestBody UserRegisterRequest userRegisterRequest){

        return new Response<>("true", "회원가입 성공", userService.register(userRegisterRequest));
    }

    //
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid
            @RequestBody UserLoginRequest userLoginRequest
    )
    {


        var response = userService.login(userLoginRequest);

        String refreshToken = response.getRefreshToken();

        long refreshTokenMaxAge = Duration.between(LocalDateTime.now(), response.getRefreshTokenExpiredAt()).getSeconds();

        ResponseCookie refreshTokenCookie =  ResponseCookie.from("refresh-token",refreshToken)
                .httpOnly(false) //true
                .path("/")
                .maxAge(refreshTokenMaxAge)
                .secure(false) //https 환경에서만 쿠키가 발동 이거는 애매하네.
                .sameSite("Strict")   //서드파티쿠키와 관련됨. 나중에 고쳐야할듯 cors관련해서 문제 발생할수도
                .build();

        System.out.println(refreshToken+" - "+refreshTokenCookie);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);

    }

    //엑세스 토큰 갱신 쿠키 값에서 refreshtoken받아옴
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> reisAccessToken(@CookieValue("refresh-token") String refreshToken){

        try{
            var response = tokenBusiness.reissueAccessToken(refreshToken);

            return ResponseEntity.ok(response);
        }catch (ApiException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){

        Cookie cookie = new Cookie("refresh-token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키를 즉시 삭제
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }



}
