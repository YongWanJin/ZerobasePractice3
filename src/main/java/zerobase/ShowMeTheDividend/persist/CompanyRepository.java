package zerobase.ShowMeTheDividend.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.ShowMeTheDividend.model.Company;
import zerobase.ShowMeTheDividend.persist.entity.CompanyEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByTicker(String ticker);

    Optional<CompanyEntity> findByCompanyName(String name);

    Optional<CompanyEntity> findByTicker(String ticker);

    // 쿼리문의 Like 연산자를 통해
    // 주어진 글자가 포함된 문자열 데이터 불러오기
    // StartingWith : 해당 문자로부터 시작하는 문자열을 찾음
    // IgnoreCase : 대소문자 구분 안함
    Page<CompanyEntity> findByCompanyNameStartingWithIgnoreCase(String s, Pageable p);
}
