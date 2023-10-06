package zerobase.ShowMeTheDividend.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.model.ScrapedResult;
import zerobase.ShowMeTheDividend.persist.CompanyRepository;
import zerobase.ShowMeTheDividend.persist.DividendRepository;
import zerobase.ShowMeTheDividend.persist.entity.CompanyEntity;
import zerobase.ShowMeTheDividend.persist.entity.DividendEntity;
import zerobase.ShowMeTheDividend.scrapper.Scraper;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    /** 배당금 정보 갱신하는 메서드 (스케쥴링).
     * 새로 지급된 배당금이 존재하면 기존 회사 배당금 정보에 추가해준다.
     * */
    @Scheduled(cron = "${scheduler.scrap.yahoo}") // application.yml 경로 참고
    public void yahooFinanceScheduler(){
        log.info("ScraperScheduler Start");
        // 1. 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();
        // 2. 회사마다 배당금 정보를 스크래핑
        for (var company : companies) {
            log.info("현재 다음 회사의 스크래핑 진행중 : " + company.getCompanyName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
                    new Company(company.getTicker(), company.getCompanyName())
            );
            // 3. DB에 존재하지 않는 값이 등장하면 저장하기
            scrapedResult.getDividendEntities().stream()
                    // dividend DTO를 dividend Entity로 변환
                    .map(e -> new DividendEntity(company.getId(), e))
                    // dividend Entity가 존재하지 않는 경우 각각 레파지토리에 저장
                    .forEach(e->{
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists) {
                            this.dividendRepository.save(e);
                        }
                    });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
