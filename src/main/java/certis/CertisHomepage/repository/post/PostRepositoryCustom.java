package certis.CertisHomepage.repository.post;

import certis.CertisHomepage.common.api.PageApi;
import certis.CertisHomepage.domain.BoardType;
import certis.CertisHomepage.web.dto.post.PostDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    PageApi<List<PostDto>> getPosts(Pageable pageable, BoardType boardType);

    PageApi<List<PostDto>> searchPosts(Pageable pageable, BoardType boardType, SearchCr criteria, String word);

}
