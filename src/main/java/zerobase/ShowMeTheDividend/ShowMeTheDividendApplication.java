package zerobase.ShowMeTheDividend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.scrapper.YahooFinanceScraper;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling // 스케쥴러 사용
@EnableCaching // 캐시 기능 사용(캐시 서버 사용)
public class ShowMeTheDividendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		// # 앱 실행
		SpringApplication.run(ShowMeTheDividendApplication.class, args);
	}
}
