package zerobase.ShowMeTheDividend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import zerobase.ShowMeTheDividend.persist.entity.MemberEntity;

import java.util.List;

public class Auth {

    /** 로그인에 필요한 DTO 객체 생성
     * */
    @Data
    @NoArgsConstructor
    public static class SignIn {
        private String username;
        private String password;
    }

    /** 회원가입에 필요한 DTO 객체 생성
     * */
    @Data
    @NoArgsConstructor
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles;

        /** 회원가입에 필요한 객체를 DTO(model)객체에서 Entity(persist)객체로 변환하는 메서드
         * */
        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .roles(this.roles)
                    .build();
        }
    }
}
