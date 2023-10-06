package zerobase.ShowMeTheDividend.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.model.Dividend;
import zerobase.ShowMeTheDividend.model.ScrapedResult;
import zerobase.ShowMeTheDividend.persist.CompanyRepository;
import zerobase.ShowMeTheDividend.persist.DividendRepository;
import zerobase.ShowMeTheDividend.persist.entity.CompanyEntity;
import zerobase.ShowMeTheDividend.persist.entity.DividendEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    /**
     * */
    @Cacheable(key = "#companyName", value = "finance") // 캐시 사용
    public ScrapedResult getDividendByCompanyName(String companyName){
        // 1. 회사 이름을 기준으로 회사 정보를 조회한다.
        // (회사가 존재하는지 확인)
        CompanyEntity companyEntity =
                this.companyRepository.findByCompanyName(companyName)
                        .orElseThrow(()->new RuntimeException("존재하지 않는 회사입니다."));
            // orElseThrow() : 값이 존재하지 않으면 해당 Exception을 발생시킨다.
            // 값이 존재하면 Optional이 벗겨진 형태의 CompanyEntity객체를 리턴한다.

        // 2. 존재가 확인된 회사의 배당금 정보를 조회한다.
        List<DividendEntity> dividendEntities =
                this.dividendRepository.findAllByCompanyId(companyEntity.getId());

        // 3. 결과들을 조합하고 반환한다.
        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());
        return new ScrapedResult(
                new Company(companyEntity.getTicker(), companyEntity.getCompanyName()),
                dividends
        );
    }

}
