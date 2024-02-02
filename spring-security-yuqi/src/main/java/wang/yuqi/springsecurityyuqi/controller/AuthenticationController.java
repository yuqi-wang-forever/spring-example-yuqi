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
import wang.yuqi.springsecurityyuqi.dto.AuthenticationRequest;
import wang.yuqi.springsecurityyuqi.util.JWTUtils;

@RestController
@RequestMapping("/api/v1/authentication")
@AllArgsConstructor
public class AuthenticationController {
    // 在config类注入自己的
    // {@link WebSecurityConfig#authenticationManager(AuthenticationConfiguration authenticationConfiguration)}
    private AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;

    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        if (null != userDetails) {
            return ResponseEntity.ok(jwtUtils.generateJSONWebToken(userDetails));
        }
        return ResponseEntity.badRequest().build();
    }
}
