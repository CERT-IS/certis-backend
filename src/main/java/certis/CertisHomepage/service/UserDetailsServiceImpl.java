package certis.CertisHomepage.service;

import certis.CertisHomepage.domain.CustomUserDetails;
import certis.CertisHomepage.domain.entity.UserEntity;
import certis.CertisHomepage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByAccount(userId)
                .orElseThrow(() -> new UsernameNotFoundException("not found loginId : " + userId));

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.id = String.valueOf(userEntity.getId());
        customUserDetails.userId = userEntity.getAccount();
        customUserDetails.password = userEntity.getPassword();
        customUserDetails.authorities = userEntity.getRoleType().toGrantedAuthorities();
        System.out.println("customUserDetails = " + customUserDetails.authorities);

        return customUserDetails;
    }

}
