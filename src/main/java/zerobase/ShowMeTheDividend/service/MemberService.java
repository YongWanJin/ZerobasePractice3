package zerobase.ShowMeTheDividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.ShowMeTheDividend.model.Auth;
import zerobase.ShowMeTheDividend.persist.MemberRepository;
import zerobase.ShowMeTheDividend.persist.entity.MemberEntity;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder; // 패스워드 암호화를 위한 객체
    private final MemberRepository memberRepository;

    // spring에서 제공하는 UserDetailsService 인터페이스에서 구현해줘야하는 메서드
    /***/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("can't find user!"));
    }

    /**
     * 회원가입
     */
    public MemberEntity register(Auth.SignUp member){
        // 중복 아이디 검사
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if(exists){
            throw new RuntimeException("username already exists!");
        }

        // 패스워드 암호화한 후에 저장
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());
        return result;
    }

    /**
     * 로그인할때 검증을 위한 메서드
     */
    public MemberEntity authenticate(Auth.SignIn member){
        return null;
    }
}
