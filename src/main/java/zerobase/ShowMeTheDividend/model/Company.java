package zerobase.ShowMeTheDividend.model;

import lombok.Builder;
import lombok.Data;

// DTO 역할을 하는 클래스
@Data
@Builder // 디자인 패턴 중 Builder 패턴을 사용할 수 있게해주는 어노테이션
//Builder 패턴 : 객체 생성시 .builder()로 시작해서 .build()로 끝나는 코딩 패턴
public class Company {
    private String ticker;
    private String name;
}
