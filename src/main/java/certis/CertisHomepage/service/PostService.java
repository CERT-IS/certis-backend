package certis.CertisHomepage.service;

import certis.CertisHomepage.common.Pagination;
import certis.CertisHomepage.common.api.PageApi;
import certis.CertisHomepage.domain.ImageEntity;
import certis.CertisHomepage.domain.PostEntity;
import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.repository.ImageRepository;
import certis.CertisHomepage.repository.PostRepository;
import certis.CertisHomepage.web.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileHandler fileHandler;
    private final ImageRepository imageRepository;


    //전체 게시물 조회
    //@Transactional(readOnly = true)
    public PageApi<List<PostDto>> getPosts(Pageable pageable){

        var list = postRepository.findAll(pageable); //이렇게만 해줘도 postEntity를 findAll해서 찾아오고 페이징과 정렬을 한 상태인것임!

        //stream으로 list에서 뽑아온 entity받아서 map으로 postDto::toDto메소드사용해서 List로
        List<PostDto> postDtos = list.stream()
                .map(PostDto::toDto)
                .collect(Collectors.toList());

                var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build();



        var response = PageApi.<List<PostDto>>builder()
                .body(postDtos)
                .pagination(pagination)
                .build()
                ;

        return response;
    }

    //개별 게시물 조회
    @Transactional(readOnly = true)
    public PostDto getPost(Long id){
        PostEntity post= postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Post Id를 찾을 수 없음");
        });

        PostDto postDto = PostDto.toDto(post);
        return postDto;
    }


    //게시물 작성
    @Transactional
    public PostDto write(PostDto postDto, UserEntity userEntity, List<MultipartFile> files) throws IOException {
        log.info("userEntity is: {}", userEntity);
        List<ImageEntity> imageList = fileHandler.parseFileInfo(files);

        PostEntity post = PostEntity.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .view(0L)
                .registeredAt(LocalDateTime.now())
                .user(userEntity)
                .build();

        userEntity.addPost(post); // Bidirectional relationship 설정

        if(!imageList.isEmpty()){
            for (ImageEntity image : imageList){
                post.addImage(imageRepository.save(image));
            }
        }


        postRepository.save(post);
        log.info("PostEntity saved with user_id = {}", post.getUser().getId());


        return PostDto.toDto(post);
    }

    //게시물 수정
    @Transactional
    public PostDto update(Long id, PostDto postDto){
        PostEntity post = postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시물 id 가 없습니다");
        });

        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        post.setModifiedAt(LocalDateTime.now());
        //이미지
        //post.setImg();

        return PostDto.toDto(post);
    }

    @Transactional
    public void delete(Long id){
        PostEntity post = postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("없는 게시물 id입니다.");
        });

        //게시글이 있는 경우 삭제 처리
        postRepository.deleteById(post.getId());
    }
}
