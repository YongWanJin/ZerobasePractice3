package zerobase.ShowMeTheDividend.config;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 검색어 자동 완성을 위한 클래스
@Configuration
public class AppConfig {

    // Trie객체는 하나만 있어야하고, 코드의 일관성을 위해
    // 별도의 생성자를 선언하는 대신 Bean 어노테이션을 활용.
    @Bean
    public Trie<String, String> trie(){
        return new PatriciaTrie<>();
    }
}
