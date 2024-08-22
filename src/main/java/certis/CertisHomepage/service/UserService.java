package certis.CertisHomepage.service;

import certis.CertisHomepage.common.error.UserErrorCode;
import certis.CertisHomepage.converter.UserConverter;
import certis.CertisHomepage.domain.RoleType;
import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.domain.UserStatus;
import certis.CertisHomepage.domain.token.TokenBusiness;
import certis.CertisHomepage.domain.token.controller.model.TokenResponse;
import certis.CertisHomepage.exception.ApiException;
import certis.CertisHomepage.repository.UserRepository;
import certis.CertisHomepage.web.dto.user.UserDto;
import certis.CertisHomepage.web.dto.user.UserLoginRequest;
import certis.CertisHomepage.web.dto.user.UserRegisterRequest;
import certis.CertisHomepage.web.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    private final TokenBusiness tokenBusiness;


    /*
    *   1.request -> entity
    *   2.entity -> save
    *   3. entity -> response
    *   4. return response
    * */
    public UserResponse register(UserRegisterRequest userRegisterRequest){

        //userId가 db에있는지 중복 체크
        checkAccountDuplicate(userRegisterRequest.getAccount());

        UserEntity user = UserEntity.builder()
                .account(userRegisterRequest.getAccount())
                .password(userRegisterRequest.getPassword())
                .username(userRegisterRequest.getName())
                .nickname(userRegisterRequest.getNickname())
                .status(UserStatus.REGISTERED)
                .level(1L) //등록하면 일단 레벨 1로고정?
                .registeredAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .exp(0L)
                .email(userRegisterRequest.getEmail())
                .roleType(RoleType.USER)
                .build();

        userRepository.save(user);

        return UserResponse.builder()
                .account(user.getAccount())
                .name(user.getUsername())
                .status(user.getStatus())
                .nickname(user.getNickname())
                .build();

    }

    /*
    *   1.account, password 를 가지고 사용자 체크
    *   2. user Entity 로그인 확인
    *   3.token 생성
    *   4.token response
    * */
    @Transactional
    public TokenResponse login(UserLoginRequest request){
        var entity = getUserWithThrow(request.getAccount(), request.getPassword());

        //TODO 토큰 생성 로직으로 변경.


        var tokenResponse = tokenBusiness.issueToken(entity);


        return tokenResponse;

    }

    public UserEntity getUserWithThrow(
            String account,
            String password
    ){
        return userRepository.findFirstByAccountAndPasswordAndStatusOrderByIdDesc(
                account,
                password,
                UserStatus.REGISTERED
        ).orElseThrow(() -> {
            return new IllegalArgumentException("ID/PW가 잘못되었습니다.");}
        );
    }

    public List<UserDto> findAll(){
        return  userRepository.findAll().stream()
                .map(UserDto::convertToDto)
                .collect(Collectors.toList());

    }


    public UserDto findUser(Long id){
        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("User Id를 찾을 수없습니다.");
        });

        return UserDto.convertToDto(user);
    }

    public void checkAccountDuplicate(String account){


        Optional<UserEntity> byAccount = userRepository.findByAccount(account);
        if (byAccount.isPresent())throw new ApiException(UserErrorCode.USER_EXIST);

    }


}
