package certis.CertisHomepage.common;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination {

    private Integer page; //현재 페이지

    private Integer size; //총 사이즈

    private Integer currentElements; //현재가지고있는 element가 몇개인지

    private Integer totalPage; //총 페이지

    private Long totalElements; // 전체 element가 몇개인지


}
