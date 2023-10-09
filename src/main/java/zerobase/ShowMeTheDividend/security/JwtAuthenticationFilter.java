package zerobase.ShowMeTheDividend.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 리퀘스트가 컨트롤러까지 가는 경로
// 리퀘스트 -> 필터링 -> 서블릿 -> 인터셉터 -> AOP -> 컨트롤러
// 이 클래스는 '필터링' 단계를 맡는다.

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 어떤 key를 기준으로 토큰을 주고받을지 설정 (Header값)
    public static final String TOKEN_HEADER = "Authorization";
    // 인증 타입 (JWT는 Bearer)
    public static final String TOKEN_PREFIX = "Bearer "; // 한칸 띄어줘야함!
    // 토큰의 유효성 검증을 위한 객체
    private final TokenProvider tokenProvider;

    /** 모든 요청을 한번 걸러주는 메소드 : 토큰 확인
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰이 있는지 없는지 확인 && 토큰이 유효한지 검증
        String token = this.resultTokenFromRequest(request);
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)){
            Authentication auth = this.tokenProvider.getAuthentication(token);
            // 인증 정보를 저장
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // 필터가 연쇄적으로 작동할 수 있도록 설정
        filterChain.doFilter(request, response);
    }

    /** 리퀘스트의 헤더로부터 토큰만 추출함
     * */
    private String resultTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(TOKEN_HEADER);
        // 토큰이 존재하고, 토큰이 JWT 종류일때
        // (즉, JWT 토큰의 형태를 띄고 있을때)
        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            // 토큰의 실질적인 내용만 추출해서 리턴
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
