package zerobase.ShowMeTheDividend.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.model.Dividend;
import zerobase.ShowMeTheDividend.model.ScrapedResult;
import zerobase.ShowMeTheDividend.model.constants.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceScraper {
    private static final String URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400;

    // # 회사의 배당금 정보 스크랩
    public ScrapedResult scrap(Company company){
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try{
            long now = System.currentTimeMillis() / 1000; // 현재시각(ms -> 초 단위변환)
            String url = String.format(URL, company.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableElement = parsingDivs.get(0);
            Element tbody = tableElement.children().get(1);

            List<Dividend> dividends = new ArrayList<>(); // 스크랩 결과 저장할 리스트
            for(Element e : tbody.children()){
                String txt = e.text();
                if(!txt.endsWith("Dividend")){
                    continue;
                }
                String[] splits = txt.split(" ");
                int month = Month.strToNum(splits[0]); // static 매서드라서 바로 호출 가능
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if(month < 0){
                    throw new RuntimeException("잘못된 월 문자열이 입력되었습니다: " + splits[0]);
                }
                dividends.add(
                        Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());
            }
            scrapResult.setDividendEntities(dividends);

        } catch(IOException e){
            e.printStackTrace();;
        }
        return scrapResult;
    }

    // # 회사의 이름 스크랩
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL, ticker, ticker);
        try {
            Document document = Jsoup.connect(url).get();
            Element titleElement = document.getElementsByTag("h1").get(0);
            String title = titleElement.text().split(" - ")[1].trim();
            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return null;
    }
}
