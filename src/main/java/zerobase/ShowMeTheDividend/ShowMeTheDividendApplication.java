package zerobase.ShowMeTheDividend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.scrapper.YahooFinanceScraper;

import java.io.IOException;

@SpringBootApplication
public class ShowMeTheDividendApplication {

	public static void main(String[] args) {
		// # 앱 실행
//		SpringApplication.run(ShowMeTheDividendApplication.class, args);

		// # 웹스크래핑 테스트1
		YahooFinanceScraper scraper = new YahooFinanceScraper();
//		var result = scraper.scrap(Company.builder().ticker("COKE").build());
		var result = scraper.scrapCompanyByTicker("COKE");
		System.out.println(result);


	}

}
