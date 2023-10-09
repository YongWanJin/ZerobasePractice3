package zerobase.ShowMeTheDividend.persist.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "MEMBER")
@Builder
public class MemberEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // 유저 아이디
    private String password; // 패스워드

    // Entity에서 List객체로 필드값을 가질때에는
    // 반드시 @ElementCollection 어노테이션을 붙여야함.
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles; // 사용자의 권한

    // spring에서 제공하는 UserDetails 인터페이스에서 구현해줘야하는 메서드들

    /** 해당 계정의 권한 목록을 리턴
     * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /** 계정의 고유값(다른 계정과 중복되지 않은 값, 유저 아이디)을 리턴
     * */
    @Override
    public String getUsername() {
        return this.username;
    }

    /** 계정의 만료 여부 리턴
     * */
    @Override
    public boolean isAccountNonExpired() {
        return true; // true : 만료 안됨(사용 가능), false : 만료됨(사용 불가)
    }

    /** 계정의 잠김 여부 리턴
     * */
    @Override
    public boolean isAccountNonLocked() {
        return true; // true : 잠기지 않음(사용 가능), false : 잠김(사용 불가)
    }

    /** 비밀번호 만료 여부 리턴
     * */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true : 만료 안됨(사용 가능), false : 만료됨(사용 불가)
    }

    /** 계정 활성화 여부 리턴
     * */
    @Override
    public boolean isEnabled() {
        return true; // true : 활성화 됨, false : 활성화 안됨
    }
}
