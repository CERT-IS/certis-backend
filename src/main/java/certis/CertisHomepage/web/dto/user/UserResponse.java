package certis.CertisHomepage.web.dto.user;

import certis.CertisHomepage.domain.UserStatus;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponse {

    private String account;


    private String name;

    private String nickname;

    private UserStatus status;

    private Long exp;

    private Long level;

}
