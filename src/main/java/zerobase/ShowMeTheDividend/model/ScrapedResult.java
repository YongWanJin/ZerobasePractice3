package zerobase.ShowMeTheDividend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Company객체와 Dividend객체<들>을 종합하여,
// 최종 웹크롤링 결과를 담는 객체를 생성하는 클래스
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class ScrapedResult {
    private Company company;
    private List<Dividend> dividendEntities;
        // 한 회사는 시간이 흐르면서 여러개의 배당금 정보를 갖는다.
    public ScrapedResult(){
        this.dividendEntities = new ArrayList<>();
    }
}
