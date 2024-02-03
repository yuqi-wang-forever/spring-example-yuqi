package wang.yuqi.springsecurityyuqi.config;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
import wang.yuqi.springsecurityyuqi.dao.UserDetailDao;


import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

    private final JwtFilterConfig jwtFilterConfig;
    private final UserDetailDao userDetailDao;
    private final String  username = "yuqi";


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

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
                .authenticationProvider(authenticationProvider())
                .csrf(AbstractHttpConfigurer::disable) // 防止csrf攻击
                .securityMatcher("**/v1/greeting/**")
                // session的创建策略为无状态
                .sessionManagement(
                        ( httpSecuritySessionManagementConfigurer ->
                                // Spring Security永远不会创建HttpSession，也永远不会使用它来获取SecurityContext
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        ))

                ;
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        // 工厂创建一个默认的编码格式
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 重写覆盖原有的方法
        return userDetailDao::loadUserByUsername;
    }

}
