package zerobase.ShowMeTheDividend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zerobase.ShowMeTheDividend.service.MemberService;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    private static final String KEY_ROLES = "roles";
    private final MemberService memberService;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    /** 토큰 생성(발급)하는 메서드
     * */
    public String generateToken(String username, List<String> roles){
        // 사용자의 권한 정보 저장
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        // 토큰 만료 시각 설정
        var now = new Date(); // 현재시각
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);// 토큰 만료 시각

        // 토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘과 비밀키
                .compact(); // 토큰 생성
    }

    /** 유저 아이디(username)을 토큰으로부터 가져옴
     * */
    public String getUsername(String token){
        return this.parseClaims(token).getSubject();
    }

    /** 토큰이 유효한지 검사하는 메서드
     * */
    public boolean validateToken(String token){
        // 토큰의 값이 빈값이다 = 유효하지 않다
        if(!StringUtils.hasText(token)) return false;

        // 토큰 만료시간이 현재 시각보다 이전이다 = 유효하지 않다
        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());

    }

    /** 토큰으로부터 claim 정보를 추출하는(파싱하는) 메서드
     * */
    private Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }

    }

    /** JWT 토큰으로부터 인증 정보를 가져오는 메서드
     * */
    public Authentication getAuthentication(String jwt){
        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));
        // 받아온 유저 데이터(userDetails)를 스프링에서 지원해주는 형태의 토큰으로 변환, 리턴
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


}
