package zerobase.ShowMeTheDividend.scrapper;

import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.model.ScrapedResult;

// YahooFinanceScraper 코드 리팩토링용 인터페이스
// (인터페이스를 통해 확장성을 꾀할 수 있다.)
public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
