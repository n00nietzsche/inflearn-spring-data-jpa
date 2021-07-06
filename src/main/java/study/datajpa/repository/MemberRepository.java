package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;

// Ctrl + P 를 누르면 넣어야 하는 것들의 타입이 나온다.
// T extends Object, ID extends Object
// 각각 엔티티와 기본키의 타입을 넣어준다.

// 인터페이스고 구현 코드가 없는데 어떻게 실행되는 걸까?
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 메소드 이름으로 쿼리 생성
    // 관례를 가진 메소드 이름으로 자동으로 쿼리 생성이 가능함
    // 단, 이름이 관례에 안 맞으면 당연히 안 된다.
    // 그런데 이름이 이상할 때도 꽤 의미 있는 힌트 메세지를 준다.
    // No property username2 found for type Member! Did you mean 'username'?
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    // 위에 있는 공식문서에서 관례를 볼 수 있다.
    // 재밌는게, `In Collection<Type>`과 같은 것도 지원한다.
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    // 어떤 것을 조회하는지에 대한 부분도 네이밍한 버전 (설명을 위해, 기능은 차이 없음)
    // 기능은 동일하다.
    List<Member> findMembersByUsernameAndAgeGreaterThan(String username, int age);
    List<Member> findTop3ByOrderByAgeDesc();
}
