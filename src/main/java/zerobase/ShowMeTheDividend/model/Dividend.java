package zerobase.ShowMeTheDividend.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// ### 한 회사의 해당 날짜에 지급한 배당금 객체를 만드는 클래스
@Data
@Builder
public class Dividend {
    private Long companyId;

    private LocalDateTime date;

    private String dividend;
}
