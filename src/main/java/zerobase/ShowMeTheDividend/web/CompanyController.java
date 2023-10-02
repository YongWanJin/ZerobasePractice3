package zerobase.ShowMeTheDividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company") // 요청 경로 중복되는 것
public class CompanyController {

    // # 검색어 자동완성 API
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword){
        return null;
    }

    // # 배당금이 존재하는 모든 회사 조회
    @GetMapping
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    // # 배당금 개별 스크래핑(관리자용)
    @PostMapping
    public ResponseEntity<?> addCompany(){
        return null;
    }

    // # 회사 DB에서 삭제(관리자용)
    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
