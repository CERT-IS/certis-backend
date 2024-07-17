package certis.CertisHomepage.service;

import certis.CertisHomepage.domain.ImageEntity;
import certis.CertisHomepage.domain.PostEntity;
import certis.CertisHomepage.domain.UserEntity;
import certis.CertisHomepage.repository.ImageRepository;
import certis.CertisHomepage.repository.PostRepository;
import certis.CertisHomepage.web.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileHandler fileHandler;
    private final ImageRepository imageRepository;


    //전체 게시물 조회
    @Transactional(readOnly = true)
    public List<PostDto> getPosts(){
        List<PostEntity> posts = postRepository.findAll();
        List<PostDto> postDtos = new ArrayList<>();
        posts.forEach(t -> postDtos.add(PostDto.toDto(t))); //
        return postDtos;
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
    public PostDto write(PostDto postDto, UserEntity user, List<MultipartFile> files) throws IOException {

        PostEntity post = new PostEntity();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUser(user); //writer



        List<ImageEntity> imageList = fileHandler.parseFileInfo(files);
        if(!imageList.isEmpty()){
            for (ImageEntity image : imageList){
                post.addImage(imageRepository.save(image));
            }
        }

        postRepository.save(post);

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
