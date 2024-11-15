package certis.CertisHomepage.controller;

import certis.CertisHomepage.common.api.PageApi;
import certis.CertisHomepage.common.error.PostErrorCode;
import certis.CertisHomepage.common.error.UserErrorCode;
import certis.CertisHomepage.domain.BoardType;
import certis.CertisHomepage.domain.entity.PostEntity;
import certis.CertisHomepage.domain.entity.UserEntity;
import certis.CertisHomepage.domain.UserStatus;
import certis.CertisHomepage.domain.token.TokenBusiness;
import certis.CertisHomepage.common.exception.ApiException;
import certis.CertisHomepage.repository.post.PostRepository;
import certis.CertisHomepage.repository.UserRepository;
import certis.CertisHomepage.repository.post.SearchCr;
import certis.CertisHomepage.service.PostService;
import certis.CertisHomepage.domain.dto.post.PostDto;
import certis.CertisHomepage.domain.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final TokenBusiness tokenBusiness;
    private final PostRepository postRepository;

    //게시판별 게시글 전체 조회
    @GetMapping("/{boardType}/all")
    public PageApi<List<PostDto>> getPosts(
            @PathVariable("boardType") String boardType,
            @PageableDefault(page = 0, size = 10) //Default가 Direction.ASC
            Pageable pageable
    ) {
        return postService.getPosts(pageable, BoardType.valueOf(boardType.toUpperCase()));
    }

    //개별 게시물 조회
    @Operation(description = "개별 게시글 보기")
    @GetMapping("/{boardType}/{id:\\d+}")   //이렇게 해버리면 swagger접근도 에러가 날수있음 모든 접근을 다잡기때문에
    public Response getPost(
            @PathVariable("boardType") String boardType,
            @PathVariable("id") Long id
    )
    {
        Optional<PostEntity> post = postRepository.findById(id);

        if(post.isEmpty() || !post.get().getBoardType().equals(BoardType.valueOf(boardType.toUpperCase()))){
            return new Response("실패", "개별 게시물 찾기 실패", new ApiException(PostErrorCode.POST_NOT_FOUND));
        }

        return new Response("성공", "개별 게시물 리턴", postService.getPost(id));


    }

    @GetMapping("/search/{boardType}")
    public PageApi<List<PostDto>> searchPosts(
            @PathVariable BoardType boardType,
            @RequestParam(value = "word", required = false)String word,
            @RequestParam(value = "criteria", required = false, defaultValue = "TITLE")SearchCr criteria,
            @PageableDefault(page = 0, size = 10)Pageable pageable
    ){
        return postService.searchPosts(pageable,boardType,criteria,word);
    }

    //게시글 작성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{boardType}/write", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Response write(
            @Valid
            @PathVariable("boardType") String boardType,
            @RequestPart("postDto") PostDto postDto,     //@ResponseBody, @RequestBody의 차이
            @RequestPart(value = "files", required = false) List<MultipartFile> files, //// @RequestPart: MultipartFile을 받음
            @RequestHeader("authorization-token") String accesstoken//@RequestHeader를 통해 헤더에 있는 토큰 값을 받아옴
            ) throws Exception {


        System.out.println("Received boardType: " + boardType);
        System.out.println("Received postDto: " + postDto);
        System.out.println("Received files: " + (files != null ? files.size() : "null"));
        System.out.println("Received accesstoken: " + accesstoken);

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

        String bt = boardType.toUpperCase();
        BoardType type;
        //NOTI도 아니고 PROJECT도 아니면 오류 발생
        try {
            type = BoardType.valueOf(bt);
        }catch (IllegalArgumentException e){
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }
        return new Response("성공", " 게시물 작성",postService.write(type, postDto, user, files));
    }


    //게시글 수정
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{boardType}/update/{id}", consumes = "multipart/form-data")
    public Response edit(
            @PathVariable("boardType") String boardType,
            @PathVariable("id") Long id,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart("postDto") PostDto postDto,
            @RequestHeader("authorization-token") String accesstoken
    ) throws IOException {


        Optional<PostEntity> post = postRepository.findById(id);

        if (post.isEmpty() || post.get().getBoardType() != BoardType.valueOf(boardType.toUpperCase())) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }


        //쓴사람이 아니라면 수정도 불가
        if(Objects.equals(post.get().getUser().getId(), tokenBusiness.validationAccessToken(accesstoken))) {
            return new Response("성공", "글 수정 성공", postService.update(id, post.get().getBoardType(), postDto, files));
        }else{
            return new Response("실패", "글 수정 실패", new ApiException(UserErrorCode.USER_NOT_CORRET));
        }


    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{boardType}/update/{id}")
    public Response getEdit(
            @PathVariable("boardType") String boardType,
            @PathVariable("id") Long id,
            @RequestHeader("authorization-token") String accesstoken
    ) {
        //원래 있던 값 받아오기
        //원래 값 반환해서 출력
        Optional<PostEntity> post = postRepository.findById(id);


        if(Objects.equals(post.get().getUser().getId(), tokenBusiness.validationAccessToken(accesstoken)) | true) {
            return new Response("성공", "글 로드 성공", postService.getPost(id));
        }else{
            return new Response("실패", "글 로드 실패", new ApiException(UserErrorCode.USER_NOT_CORRET));
        }
    }

    //게시글 삭제
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{boardType}/delete/{id}")
    public Response delete(
            @PathVariable("boardType") String boardType,
            @PathVariable Long id,
            @RequestHeader("authorization-token") String accesstoken
    ){
        // 이것도 마찬가지로, JWT(로그인 관련) 공부를 하고나서 현재 이 요청을 보낸 로그인된 유저의 정보가
        // 게시글의 주인인지 확인하고, 맞으면 삭제 수행 후 리턴해주고, 틀리면 에러 리턴
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isEmpty() || post.get().getBoardType() != BoardType.valueOf(boardType.toUpperCase())) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        if(post.get().getUser().getId() == tokenBusiness.validationAccessToken(accesstoken)) {
            postService.delete(id);
            return new Response<>("성공", "글 삭제 성공", null);
        }else{
            return new Response("실패", "글 삭제 실패", new ApiException(UserErrorCode.USER_NOT_CORRET));
        }
    }



}
