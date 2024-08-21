package certis.CertisHomepage.service;

import certis.CertisHomepage.domain.Pagination;
import certis.CertisHomepage.common.api.PageApi;
import certis.CertisHomepage.domain.*;
import certis.CertisHomepage.domain.entity.ImageEntity;
import certis.CertisHomepage.domain.entity.PostEntity;
import certis.CertisHomepage.domain.entity.UserEntity;
import certis.CertisHomepage.repository.ImageRepository;
import certis.CertisHomepage.repository.PostRepository;
import certis.CertisHomepage.repository.UserRepository;
import certis.CertisHomepage.domain.dto.post.PostDto;
import certis.CertisHomepage.domain.dto.post.GetPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileHandler fileHandler;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;



    //전체 게시물 조회
    //@Transactional(readOnly = true)
    public PageApi<List<PostDto>> getPosts(Pageable pageable, BoardType boardType){

        //다들고 와버리면 데이터가 많을수록 올래걸릴거임. -> querydsl
        var list = postRepository.findByBoardType(boardType, pageable); //이렇게만 해줘도 postEntity를 findAll해서 찾아오고 페이징과 정렬을 한 상태인것임!

        //stream으로 list에서 뽑아온 entity받아서 map으로 postDto::toDto메소드사용해서 List로
        List<PostDto> postDtos = list.stream()
                .map(PostDto::toDto)
                .collect(toList());

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
    public GetPostResponse getPost(Long id){

        //지금 http://localhost:8080/photo/photo/20240727/860469845873500.jpg 이런 형식으로 내려주는데
        // ../으로 경로 해킹하는것 관련 방어 로직을 더 짜야할듯.

        PostEntity post= postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Post Id를 찾을 수 없음");
        });
        log.info("게시물을 찾았습니다.");
        post.increaseViewCnt(); //게시물 조회수 증가.

        // Hibernate의 initialize 메서드를 사용하여 UserEntity 초기화
        //Hibernate.initialize(post.getUser());

        Long userId = post.getUser().getId();
        UserEntity user = userRepository.findByIdAndStatus(userId, UserStatus.REGISTERED);

        log.info("user의 userName은 {}", user.getUsername());
        if (user == null) {
            log.info("userentity is null");
            throw new IllegalArgumentException("User 정보가 없습니다.");
        }

        //이미지에 접근할수있음.
        var list = imageRepository.findByPostId(id);

        List<String> imageUrlList = list.stream()
                .map(image -> convertToUrl(image.getImgUrl()))
                .collect(toList());

        GetPostResponse response = GetPostResponse
                .toDto(post, imageUrlList);


        return response;
    }

    //URL에서는 슬래시(/)를 사용해야 하므로, 역슬래시를 슬래시로 교체.
    private String convertToUrl(String storedImageUrl) {
        return "/" + storedImageUrl.replace("\\","/");
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
                .boardType(postDto.getBoardType())
                .registeredAt(LocalDateTime.now())
                .user(userEntity)
                .build();

        //userEntity.addPost(post); // Bidirectional relationship 설정

        if(!imageList.isEmpty()){
            for (ImageEntity image : imageList){
                //원래 이미지이름이 빈것이아니라면 저장하기.
                if(!image.getOriginalImgName().isBlank()){
                    post.addImage(imageRepository.save(image));
                }
            }
        }

        postRepository.save(post);
        log.info("PostEntity saved with user_id = {}", post.getUser().getId());


        return PostDto.toDto(post);
    }

    //게시물 수정
    @Transactional
    public PostDto update(Long id, PostDto postDto, List<MultipartFile> files) throws IOException {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시물 id 가 없습니다");
        });

        post.setTitle(postDto.getContent());
        post.setContent(postDto.getContent());
        post.setModifiedAt(LocalDateTime.now());

        //기존 post에 있는 이미지 정보가져오기
        List<ImageEntity> existingImages = post.getPostImages();

        //있는 이미지들 삭제하고 새거 받아오기
        deleteImages(existingImages);
        post.getPostImages().clear();


        //새로운 이미지를 저장
        List<ImageEntity> newImageEntities = fileHandler.parseFileInfo(files);
        for(ImageEntity newImage : newImageEntities){
            newImage.setPost(post);
            //원래 이미지이름이 빈것이아니라면 저장하기.
            if(!newImage.getOriginalImgName().isBlank()){
                post.addImage(imageRepository.save(newImage));
                log.info("post 수정");
            }
        }
        //수정된 게시글을 저장.
        postRepository.save(post);


        // 수정된 정보를 반영한 DTO 리턴
        return PostDto.toDto(post);
    }

    private void deleteImages(List<ImageEntity> imageToDelete) {
        for(ImageEntity image : imageToDelete){
            String imagePath = image.getImgUrl();
            File file = new File(imagePath);

            //파일존재여부에따라서
            if(!file.exists()){
                log.warn("다음 경로에 파일이 존재하지 않음 : " + imagePath);
            }else{
                if (!file.delete()){
                    log.error("파일 삭제에 실패했습니다. : " + imagePath);
                    throw new RuntimeException("파일 삭제 실패");
                }
                imageRepository.delete(image);
            }

        }
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
