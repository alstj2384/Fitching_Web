package ssamppong.fitchingWeb.global.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ssamppong.fitchingWeb.global.ResponseUtil;
import ssamppong.fitchingWeb.global.jwt.ErrorCode;
import ssamppong.fitchingWeb.global.login.service.CustomUserDetailService;
import ssamppong.fitchingWeb.entity.User;
import ssamppong.fitchingWeb.repository.UserRepository;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
@Getter
@Slf4j
@Transactional
public class JwtService {
    private final Key key;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;
    private final ResponseUtil responseUtil;

    public JwtService(@Value("${jwt.secret}")String secretKey, UserRepository userRepository
            , CustomUserDetailService customUserDetailService, ResponseUtil responseUtil) {
        byte[] decode = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decode);
        this.userRepository = userRepository;
        this.customUserDetailService = customUserDetailService;
        this.responseUtil = responseUtil;
    }


    /**
     * AccessToken 생성
     */
    public String createAccessToken(String email){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))
                .claim("email", email)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * AccessToken 응답 헤더에 추가
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken){
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 AccessToken : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 응답 헤더에 추가
     */
    public void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken){
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, BEARER + accessToken);
        response.setHeader(refreshHeader, BEARER + refreshToken);
        log.info("AccessToken, RefreshToken 헤더 설정 시작");
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {} ", refreshToken);
        log.info("AccessToken, RefreshToken 헤더 설정 완료");
    }

    public void setFailureMessage(HttpServletResponse response) {
        try{
            responseUtil.setFailureResponse(response, ErrorCode.EXPIRED_TOKEN);
        } catch(Exception e){
            log.info("{}", e);
        }
    }

    /**
     * 헤더에서 AccessToken 추출 (Bearer 삭제)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(accessHeader))
//                .filter(accessToken -> accessToken.startsWith(BEARER))
//                .map(accessToken -> accessToken.replace(BEARER, ""));

        String token = request.getHeader(accessHeader);
//        log.info("accessToken 토큰 추출 : {}", token);
        if(token != null && token.startsWith(BEARER)){
            return Optional.of(token.substring(BEARER.length()));
        }
        return Optional.empty();
    }

    /**
     * 헤더에서 RefreshToken 추출 (Bearer 삭제)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(refreshHeader);
//        log.info("refresh 토큰 추출 : {}", token);
        if(token != null && token.startsWith(BEARER)){
            return Optional.of(token.substring(BEARER.length()));
        }
        return Optional.empty();
    }


    /**
     * AccessToken에서 이메일 추출
     */
    public Optional<String> extractEmail(String accessToken){
        validateToken(accessToken);

        String email = (String) parseClaims(accessToken).get("email");
//        log.info("AccessToken Email parsing : {}", email);
        return Optional.ofNullable(email);
    }


    /**
     * RefreshToken DB 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken){
        log.info("RefreshToken DB 저장 시작");
        log.info("email : {}", email);
        log.info("refreshToken : {}", refreshToken);
        Optional<User> findUser = userRepository.findByEmail(email);
        findUser.ifPresentOrElse(user ->
                user.updateRefreshToken(refreshToken),
                () -> new Exception("일치하는 회원이 없습니다"));
        log.info("RefreshToken DB 저장 완료");
    }


    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return true;
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

}
