package zerobase.ShowMeTheDividend.web;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.ShowMeTheDividend.service.FinanceService;

@RestController
@RequestMapping("/finance") // 요청 경로 중복되는 것
@AllArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    // # 특정한 회사의 배당금을 조회하는 API
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        var result = this.financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }
}
