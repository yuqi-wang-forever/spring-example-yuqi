package wang.yuqi.springsecurityyuqi.config;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.StringTemplate.STR;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurityConfig {
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final JwtFilterConfig jwtFilterConfig;
    private final String  username = "yuqi";

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

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        (requests) ->
                                requests
                                        .anyRequest()
                                        .authenticated());
        http.httpBasic(withDefaults())
                .addFilterBefore(jwtFilterConfig, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider());
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new DaoAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        // 工厂创建一个默认的编码格式
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            // 重写覆盖原有的方法
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return APPLICATION_USER.stream()
                        .filter(userDetails -> userDetails.getUsername().equals(username))
                        .findFirst()
                        .orElseThrow(() -> new UsernameNotFoundException(STR."Unfortunately \{username} is not found"))
                        ;
            }
        };
    }

}
