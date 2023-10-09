package zerobase.ShowMeTheDividend.web;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.persist.entity.CompanyEntity;
import zerobase.ShowMeTheDividend.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/company") // 요청 경로 중복되는 것 한번에 지정
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CacheManager redisCacheManager; // 캐시 삭제용

    /** 검색어 자동완성 API
     * */
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword){
//        var result = this.companyService.autoComplete(keyword);
        var result = this.companyService.getCompanyNameByKeyword(keyword);
        return ResponseEntity.ok(result);

    }


    /** 회사 목록 조회 API
     * */
    // 페이지 단위로 나눠서 조회한다. (Springframwork의 Pageable 객체 사용)
    // 사용가능한 url 파라미터들
    // size : 한번에 보여줄 데이터의 개수
    // page : 몇번째 페이지를 보여줄지 (주의! 0번째 페이지부터 시작함!)
    //
    @GetMapping
    @PreAuthorize("hasRole('READ')") // 읽기 권한이 있는 유저(사용자)만 호출 가능한 API

    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    /* 배당금 개별 스크래핑 API(관리자용)
    **/
    @PostMapping
//    @PreAuthorize("hasRole('WRITE')") // 쓰기 권한이 있는 유저(관리자)만 호출 가능한 API
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("Ticker값이 입력되지 않았습니다.");
        }
        Company company = this.companyService.save(ticker);
        // 검색 이력을 자동완성 목록에 저장
        this.companyService.addAutocompleteKeyword(company.getName());
        return ResponseEntity.ok(company);
    }

    /** 회사 DB에서 삭제(관리자용)
     * */
//    @DeleteMapping("/{ticker}")
//    @PreAuthorize("hasRole('WRITE')")
//    public ResponseEntity<?> deleteCompany(@PathVariable String ticker){
//        String companyName = this.companyService.deleteCompany(ticker);
//        this.clearFinanceCache(companyName);
//        return ResponseEntity.ok(companyName);
//    }
//
//    private void clearFinanceCache(String companyName) {
//        this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
//    }
}
