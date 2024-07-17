package certis.CertisHomepage.web.controller;

import certis.CertisHomepage.common.api.Api;
import certis.CertisHomepage.domain.token.controller.model.TokenResponse;
import certis.CertisHomepage.service.UserService;
import certis.CertisHomepage.web.dto.user.UserLoginRequest;
import certis.CertisHomepage.web.dto.user.UserRegisterRequest;
import certis.CertisHomepage.web.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

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
    public Api<TokenResponse> login(
            @Valid
            @RequestBody UserLoginRequest userLoginRequest
    )
    {

        var response = userService.login(userLoginRequest);
        return Api.OK(response);

    }

}
