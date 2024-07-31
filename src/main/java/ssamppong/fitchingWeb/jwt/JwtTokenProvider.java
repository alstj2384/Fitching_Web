package ssamppong.fitchingWeb.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ssamppong.fitchingWeb.config.security.CustomUserDetailService;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private final Key key;
    private final CustomUserDetailService customUserDetailService;

    public JwtTokenProvider(@Value("${jwt.secret}")String secretKey, CustomUserDetailService customUserDetailService) {
        byte[] decode = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decode);
        this.customUserDetailService = customUserDetailService;
    }

    public JwtToken generate(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + 1000 * 60 * 30);
//        Date accessTokenExpiresIn = new Date(now + 1000 * 15);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 1000 * 60 * 60 * 24 * 14))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String accessToken){

        Claims claims = parseClaims(accessToken);

        if(claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails user = customUserDetailService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return true;
    }
}
