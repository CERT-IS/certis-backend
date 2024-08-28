package certis.CertisHomepage.repository.post;

import certis.CertisHomepage.common.Pagination;
import certis.CertisHomepage.common.api.PageApi;
import certis.CertisHomepage.domain.BoardType;
import certis.CertisHomepage.domain.entity.PostEntity;
import certis.CertisHomepage.domain.QPostEntity;
import certis.CertisHomepage.domain.dto.post.PostDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PageApi<List<PostDto>> getPosts(Pageable pageable, BoardType boardType) {

        QPostEntity post = QPostEntity.postEntity;


        // 정렬 기준 정의 (최신순으로 정렬,내림차순으로 정렬)
        OrderSpecifier<?> orderByRegisteredAtDesc = post.registeredAt.desc();
        List<PostEntity> posts = queryFactory.selectFrom(post)
                .where(boardTypeEq(boardType))
                .orderBy(orderByRegisteredAtDesc)
                .offset(pageable.getOffset())   //몇 번째 페이지부터 시작할 것 인지
                .limit(pageable.getPageSize())  //페이지당 몇개의 데이터를 보여줄껀지
                .fetch();


        //총 게시물 수를 조회
        List<Long> res = queryFactory.select(post.count())
                .from(post)
                .where(boardTypeEq(boardType))
                .fetch();  //fetchCount등이 deprecated되서 fatch로 처리했음

        long total = res.isEmpty() ? 0 : res.get(0);

        List<PostDto> postDtos = posts.stream()
                .map(PostDto::toDto)
                .toList();

        Pagination pagination = Pagination.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .currentElements(posts.size())
                .totalElements(total)
                .totalPage((int) Math.ceil((double) total / pageable.getPageSize()))
                .build();

        PageApi<List<PostDto>> response = PageApi.<List<PostDto>>builder()
                .body(postDtos)
                .pagination(pagination)
                .build();

        return response;
    }

    @Override
    public PageApi<List<PostDto>> searchPosts(Pageable pageable, BoardType boardType, SearchCr criteria, String word) {

        QPostEntity post = QPostEntity.postEntity;

        //보드타입과 검색조건이 각각 일치하는 쿼리를 작성할것이다.
        BooleanExpression condition = norm(criteria, word)
                .and(boardTypeEq(boardType));

        List<PostEntity> posts = queryFactory.selectFrom(post)
                .where(condition)
                .orderBy(post.registeredAt.desc())
                .offset(pageable.getOffset())   //몇 번째 페이지부터 시작할 것 인지
                .limit(pageable.getPageSize())  //페이지당 몇개의 데이터를 보여줄껀지
                .fetch();


        //총 게시물 수를 조회
        List<Long> res = queryFactory.select(post.count())
                .from(post)
                .where(boardTypeEq(boardType))
                .fetch();  //fetchCount등이 deprecated되서 fatch로 처리했음

        long total = res.isEmpty() ? 0 : res.get(0);

        List<PostDto> postDtos = posts.stream()
                .map(PostDto::toDto)
                .toList();

        Pagination pagination = Pagination.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .currentElements(posts.size())
                .totalElements(total)
                .totalPage((int) Math.ceil((double) total / pageable.getPageSize()))
                .build();

        PageApi<List<PostDto>> response = PageApi.<List<PostDto>>builder()
                .body(postDtos)
                .pagination(pagination)
                .build();

        return response;


    }

    private BooleanExpression boardTypeEq(BoardType boardType){
        return boardType != null ? QPostEntity.postEntity.boardType.eq(boardType) : null;
    }

    //검색 조건
    private BooleanExpression norm(SearchCr criteria, String word){
        QPostEntity post = QPostEntity.postEntity;

        if(word == null || word.isEmpty()){
            return null;
        }

        switch (criteria){
            case TITLE -> {
                return post.title.containsIgnoreCase(word);
            }
            case CONTENT -> {
                return post.content.containsIgnoreCase(word);
            }
            case WRITER -> {
                return post.user.username.containsIgnoreCase(word);
            }
            case TITLE_CONTENT -> {
                return post.title.containsIgnoreCase(word)
                        .or(post.content.containsIgnoreCase(word));
            }
            default -> {
                return null;
            }
        }
    }

}
