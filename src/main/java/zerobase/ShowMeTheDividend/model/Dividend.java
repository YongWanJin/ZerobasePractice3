package zerobase.ShowMeTheDividend.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Dividend {
    private Long companyId;

    private LocalDateTime date;

    private String dividend;
}
