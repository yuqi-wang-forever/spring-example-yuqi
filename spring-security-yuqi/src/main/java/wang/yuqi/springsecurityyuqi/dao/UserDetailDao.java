package wang.yuqi.springsecurityyuqi.dao;

import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Repository
public class UserDetailDao{
    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final List<UserDetails> APPLICATION_USER = List.of(
            new User(
                    "yuqi"
                    ,"wang"
                    , Collections.singleton(new SimpleGrantedAuthority(ROLE_ADMIN))
            ),
            new User(
                    "liu"
                    ,"yang"
                    ,Collections.singleton(new SimpleGrantedAuthority(ROLE_ADMIN))
            )
    ) ;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return APPLICATION_USER.stream()
                .filter(userDetails -> userDetails.getUsername().equals(username))
                .findFirst()
                //.orElseThrow(() -> new UsernameNotFoundException(STR."Unfortunately \{username} is not found"))
                .orElseThrow(() -> new UsernameNotFoundException("Unfortunately" + username + "is not found"))
                ;
    }

}
