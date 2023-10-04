package zerobase.ShowMeTheDividend.persist.entity;

import lombok.*;
import zerobase.ShowMeTheDividend.model.Company;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "COMPANY")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;

    private String companyName;

    /** 이 생성자를 호출하면 DTO객체에서 Entity객체로 변환할 수 있다.
     * Entity객체는 DB에 저장 및 추출할때에, DTO객체는 앱 내부에서 다뤄질때에 사용된다.
     * */
    public CompanyEntity(Company company){
        this.ticker = company.getTicker();
        this.companyName = company.getName();
    }
}
