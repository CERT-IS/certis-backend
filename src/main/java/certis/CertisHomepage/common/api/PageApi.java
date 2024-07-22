package certis.CertisHomepage.common.api;

import certis.CertisHomepage.common.Pagination;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageApi<T> {

    //Pagenation과 data body를 받아준다.

    private T body;

    private Pagination pagination;
}
