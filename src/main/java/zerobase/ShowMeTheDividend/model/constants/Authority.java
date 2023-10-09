package zerobase.ShowMeTheDividend.model.constants;

/** 회원 관리 기능의 권한 종류
 * */
public enum Authority {
    ROLE_READ,
    ROLE_WRITE;
    // 작명법 : ROLE_권한이름
    // 이 데이터를 어노테이션에서 불러올때에는 언더바 뒤의 권한이름만 써준다.
}
