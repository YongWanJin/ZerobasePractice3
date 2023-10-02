package zerobase.ShowMeTheDividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance") // 요청 경로 중복되는 것
public class FinanceController {

    // # 회사의 배당금 조회 API
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinace(@PathVariable String companyName){
        return null;
    }
}
