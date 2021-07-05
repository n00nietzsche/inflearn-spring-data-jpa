package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

// `JpaRepository`를 상속하는 것만으로도 스프링이 컴포넌트 스캔 과정에서 인식을 하고, 구현체를 꽂아 넣음
// `@Repository` 애노테이션 생략해도 됨
// 사실 `@Repository` 속에는 JPA의 예외를 스프링의 공통 예외로 변환할 수 있는 기능도 포함하고 있음
public interface TeamRepository extends JpaRepository<Team, Long> {
}
