package wang.yuqi.springsecurityyuqi.config;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import wang.yuqi.springsecurityyuqi.dao.UserDetailDao;
import wang.yuqi.springsecurityyuqi.util.JWTUtils;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class JwtFilterConfig extends OncePerRequestFilter {


    private final UserDetailDao userDetailDao;
    private final JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        if (null == header || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
            final String token = header.substring(7);
            final String email = jwtUtils.extractUsername(token);
        if (null != email && null ==  SecurityContextHolder.getContext().getAuthentication()) {
            UserDetails userDetails = userDetailDao.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtils.isValidToken(token,userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        doFilter(request, response, filterChain);
    }
}
