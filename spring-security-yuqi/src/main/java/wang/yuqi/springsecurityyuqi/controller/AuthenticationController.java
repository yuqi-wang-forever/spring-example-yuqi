package wang.yuqi.springsecurityyuqi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;
import wang.yuqi.springsecurityyuqi.dao.UserDetailDao;
import wang.yuqi.springsecurityyuqi.dto.AuthenticationRequest;
import wang.yuqi.springsecurityyuqi.util.JWTUtils;

@RestController
@RequestMapping("/api/v1/authentication")
@AllArgsConstructor
public class AuthenticationController {
    // 在config类注入自己的
    // {@link WebSecurityConfig#authenticationManager(AuthenticationConfiguration authenticationConfiguration)}
    private final AuthenticationManager authenticationManager;
    private final UserDetailDao userDetailDao;
    private final JWTUtils jwtUtils;

    @GetExchange("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password())
        );
        UserDetails userDetails = userDetailDao.loadUserByUsername(authenticationRequest.username());
        if (null != userDetails) {
            return ResponseEntity.ok(jwtUtils.generateJSONWebToken(userDetails));
        }
        return ResponseEntity.badRequest().build();
    }
}
