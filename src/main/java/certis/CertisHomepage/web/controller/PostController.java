package certis.CertisHomepage.web.controller;

import certis.CertisHomepage.common.api.PageApi;
import certis.CertisHomepage.common.error.UserErrorCode;
import certis.CertisHomepage.domain.PostEntity;
import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.domain.UserStatus;
import certis.CertisHomepage.domain.token.TokenBusiness;
import certis.CertisHomepage.exception.ApiException;
import certis.CertisHomepage.repository.PostRepository;
import certis.CertisHomepage.repository.UserRepository;
import certis.CertisHomepage.service.PostService;
import certis.CertisHomepage.web.dto.post.PostDto;
import certis.CertisHomepage.web.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final TokenBusiness tokenBusiness;
    private final PostRepository postRepository;

    //전체 게시글 조회
    @GetMapping("/noti/all")
    public PageApi<List<PostDto>> getPosts(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) //Default가 Direction.ASC
        Pageable pageable
    ) {
        return postService.getPosts(pageable);
    }

    //개별 게시물 조회
    @Operation(description = "개별 게시글 보기")
    @GetMapping("/noti/{id}")
    public Response getPost(
            @PathVariable("id") Long id
    )
    {
        return new Response("성공", "개별 게시물 리턴", postService.getPost(id));
    }


    //게시글 작성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/noti/write")
    public Response write(
            @Valid
            @RequestPart("postDto") PostDto postDto,     //@ResponseBody, @RequestBody의 차이
            @RequestPart("files") List<MultipartFile> files, //// @RequestPart: MultipartFile을 받음
            @RequestHeader("authorization-token") String accesstoken//@RequestHeader를 통해 헤더에 있는 토큰 값을 받아옴
            ) throws Exception {
        // 원래 로그인을 하면, User 정보는 세션을 통해서 구하고 주면 되지만,
        // 지금은 JWT 로그인은 생략하고, 임의로 findById 로 유저 정보를 넣어줌.



        //TODO


        //System.out.println("userId= "+ userId);
        /*//user를 못찾아오고있음. 반면 pid는 잘 가져오는데.
        Long pid = tokenBusiness.validationAccessToken(accesstoken);

        UserEntity user = userRepository.findById(pid).orElseThrow(() -> {
            return new ApiException(UserErrorCode.USER_NOT_FOUND);
        });

        return new Response("성공", " 게시물 작성",postService.write(postDto, user, files));*/

        //그래서 결국 쿼리메소드로 만들어서 등록된것과 id를 같이 찾도록했다.그렇게 하니 게시물 작성은 완료.
        UserEntity user = userRepository.findByIdAndStatus(tokenBusiness.validationAccessToken(accesstoken), UserStatus.REGISTERED);
        if (user == null){
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        return new Response("성공", " 게시물 작성",postService.write(postDto, user, files));



    }


    //게시글 수정
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/noti/update/{id}")
    public Response edit(
            @RequestBody PostDto postDto,
            @PathVariable Long id
    ){

        // 추후에 JWT 로그인을 배우고나서 적용해야할 것

        // 1. 현재 요청을 보낸 유저의 JWT 토큰 정보(프론트엔드가 헤더를 통해 보내줌)를 바탕으로
        // 현재 로그인한 유저의 정보가 PathVariable로 들어오는 BoardID 의 작성자인 user정보와 일치하는지 확인하고
        // 맞으면 아래 로직 수행, 틀리면 다른 로직(ResponseFail 등 커스텀으로 만들어서) 수행
        // 이건 if문으로 처리할 수 있습니다. * 이 방법 말고 service 내부에서 확인해도 상관 없음

        Optional<PostEntity> post = postRepository.findById(id);
        if(post.isPresent())log.info("post exist");
        else {
            log.info("post not exist");
        }
        return new Response("성공", "글 수정 성공", postService.update(id, postDto));
    }

    //게시글 삭제
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/noti/delete/{id}")
    public Response delete(
            @PathVariable Long id
    ){
        // 이것도 마찬가지로, JWT(로그인 관련) 공부를 하고나서 현재 이 요청을 보낸 로그인된 유저의 정보가
        // 게시글의 주인인지 확인하고, 맞으면 삭제 수행 후 리턴해주고, 틀리면 에러 리턴

        postService.delete(id);
        return new Response<>("성공", "글 삭제 성공", null);
    }

}
