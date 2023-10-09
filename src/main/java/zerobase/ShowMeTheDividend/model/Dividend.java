package zerobase.ShowMeTheDividend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

// ### 한 회사의 해당 날짜에 지급한 배당금 객체를 만드는 클래스
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dividend {
//    private Long companyId;

    @Autowired
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화에 쓰일 serializer 설정
    @JsonDeserialize(using = LocalDateDeserializer.class) // 역직렬화에 쓰일 serializer 설정
    private LocalDateTime date;

    private String dividend;
}
