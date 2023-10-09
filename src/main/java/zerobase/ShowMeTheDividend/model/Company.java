package zerobase.ShowMeTheDividend.model;
// model : DTO 역할을 하는 클래스가 모여있는 패키지

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ### 회사 정보 객체를 만드는 클래스
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private String ticker;
    private String name;
}
