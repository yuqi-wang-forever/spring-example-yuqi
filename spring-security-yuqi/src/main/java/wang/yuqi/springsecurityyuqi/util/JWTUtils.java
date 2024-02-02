package wang.yuqi.springsecurityyuqi.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.security.Key;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {

    public static final String SECRET = "whatever";
    // byte[] bytes = Decoders.BASE64.decode(SECRET);
    byte[] bytes = SECRET.getBytes();
    Key key = Keys.hmacShaKeyFor(bytes);

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    public static final long EXPIRATION_TIME = 864_000_000;

    public static final long EXPIRATION_TIME_MS = EXPIRATION_TIME * 1000;

    public String generateJSONWebToken(@NonNull Map<String, Object> claims, @NonNull UserDetails userDetails) {
       return createJSONWebToken(claims, userDetails);
    }
    public String generateJSONWebToken(@NonNull UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createJSONWebToken(claims, userDetails);
    }

    public String  createJSONWebToken(Map<String,Object> claims,UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Duration.ofHours(2L).toMillis()))
                // 根据密钥来自动判断使用哪一种加密算法
                .signWith(key).compact();
    }

    public boolean isValidToken(@NonNull String token,@NonNull UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpiredToken(token);
    }

    private boolean isExpiredToken(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(@NonNull String  token) {
        return extractClaims(token,Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // 声明是否有效
    private Boolean hasClaim(String token,String claimName){
        Claims claims = extractAllClaims(token);
        return claims != null && claims.get(claimName) != null;
    }

    // JWT失效时间
    public Date extractExpiration(String token) {
        return  extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        // byte[] bytes = Decoders.BASE64.decode(SECRET);

       return Jwts.parser().verifyWith((PublicKey) key).build().parseSignedClaims(token).getPayload();
    }
}
