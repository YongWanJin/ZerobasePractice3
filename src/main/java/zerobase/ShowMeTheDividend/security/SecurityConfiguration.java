package zerobase.ShowMeTheDividend.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter authenticationFilter;

    /**어떤 경로에서 어떤 권한을 필요로하는지 (실제 서비스 관련)*/
    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http
                // httpBasic, csrf는 사용하지 않음(disable)
                // (RESTApi를 개발하려고 하기 때문)
//                .httpBasic().disable()
                .csrf().disable()

                // 세션을 사용하지 않음(stateless, 상태정보를 저장하지 않음)
                // (RESTApi를 개발하려고 하기 때문)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 권한 설정 : 로그인 및 회원가입 페이지는 아무런 권한 없이도 접근 가능해야함
                .authorizeRequests()
                .antMatchers("/**/signup", "/**/signin").permitAll()
                .and()

                // h2-console 접속을 위한 설정
                .headers().frameOptions().disable()
                .and()

                // 필터가 작동할 순서 설정
                // (후자는 스프링에서 제공하는 자체 필터)
                .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
    /**어떤 경로에서 어떤 권한을 필요로하는지 (개발 편의성 관련)*/
    @Override
    public void configure(final WebSecurity web) throws Exception{
        web.ignoring()
                .antMatchers("h2-console/**"); // ** : 어떤 경로든 다 포함한다
        // 개발의 편의성을 위해 h2-console/** 경로에는
        // 인증 정보가 없어도 접근할 수 있게 설정.
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
