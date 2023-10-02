package zerobase.ShowMeTheDividend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedResult {
    private Company company;
    private List<Dividend> dividendEntities; // 한 회사는 여러개의 배당금 정보를 갖는다.
    public ScrapedResult(){
        this.dividendEntities = new ArrayList<>();
    }
}
