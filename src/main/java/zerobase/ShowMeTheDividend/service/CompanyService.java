package zerobase.ShowMeTheDividend.service;

import lombok.*;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.model.ScrapedResult;
import zerobase.ShowMeTheDividend.persist.CompanyRepository;
import zerobase.ShowMeTheDividend.persist.DividendRepository;
import zerobase.ShowMeTheDividend.persist.entity.CompanyEntity;
import zerobase.ShowMeTheDividend.persist.entity.DividendEntity;
import zerobase.ShowMeTheDividend.scrapper.Scraper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Trie trie; // Trie객체에 회사명을 저장

    // # 스크래핑한 데이터들을 저장하는 메소드
    // '배당금 개별 스크래핑 API(관리자용)'에서 호출한다.
    public Company save(String ticker){
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if(exists){
            throw new RuntimeException("이미 존재하는 회사입니다: " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker){
        // * ticker를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(company)){
            throw new RuntimeException("존재하지 않는 회사입니다: " + ticker);
        }

        // * Company객체를 기준으로 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        // * 스크래핑 결과 리턴
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividendEntities().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());
        this.dividendRepository.saveAll(dividendEntities);
        return company;
    }

    // # DB에 존재하는 회사들 모두 불러오는 메소드
    // '회사 목록 조회 API'에서 호출한다.
    public Page<CompanyEntity> getAllCompany(Pageable pageable){
        return this.companyRepository.findAll(pageable);
        // 실무에서는 엄청 많은 데이터를 저장해야하므로, 모두 보여줄 이유가 없음.
        // 페이지 단위로 보여주는 객체 pagable을 사용.
    }

    // # Trie객체에 회사명을 저장하는 메소드
    //  '배당금 개별 스크래핑 API(관리자용)'에서 같이 호출한다.
    // 속도는 빠르지만 메모리를 많이 잡아먹는다.
    public void addAutocompleteKeyword(String keyword){
        this.trie.put(keyword, null); // key만 필요하고 value는 필요없음.
    }

    // # Trie 객체에서 회사명을 조회하는 메소드 (10개 제한)
    // '검색어 자동완성 API'에서 호출한다.
    // (keyword로 시작하는 단어들을 리턴한다.)
    public List<String> autoComplete(String keyword){
        return (List<String>) this.trie.prefixMap(keyword)
                .keySet()
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    // # Trie 객체에 저장된 회사명을 삭제하는 메소드
    public void deleteAutoCompleteKeyword(String keyword){
        this.trie.remove(keyword);
    }

    // # DB로부터 검색 결과를 직접 가져오는 메소드.
    // 코드는 간단하지만, DB에 과부하를 줄 수 있다.
    public List<String> getCompanyNameByKeyword(String keyword){
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities =
                this.companyRepository.findByCompanyNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(e -> e.getCompanyName())
                .collect(Collectors.toList());
    }

}











