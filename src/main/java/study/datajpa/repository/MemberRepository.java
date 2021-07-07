package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
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

    // `@Query` 애노테이션을 이용하여
    // 엔티티에 적용한 `@NamedQuery`의 이름을 불러와서 메소드에 적용시킬 수 있다.
    // 사실, `@Query` 애노테이션을 주석처리해도 잘 동작한다. (관례에 의해)
    // 타입(`Member`)에 `@NamedQuery`가 있으면 그걸 우선으로 실행하고,
    // 없으면 그 때 관례대로 메소드 이름에 맞추어 쿼리를 만드는 것이다.
    // 우선순위를 바꿀 수는 있지만 손댈 이유는 없다.
    // 사실 이 기능보다는 리포지토리 메소드에 바로 쿼리를 정의하는 방법을 더 많이 쓴다.
    // 일반 `JPQL`에 비해 `@NamedQuery`가 좋은 이유는 애플리케이션 로딩 시점에 에러를 알 수 있다는 점이다.
    // 일반 `JPQL`은 단순히 문자열이라 실제 쿼리가 실행됐을 때야 에러를 만날 수 있다.
    @Query(name = "Member.findByUsername")
    // `@NamedQuery`에서 작성한 것처럼 JPQL 에 명확히 파라미터(:username)가 있다면,
    // `@Param` 애노테이션을 사용해야 한다.
    List<Member> findByUsername(@Param("username") String username);

    // 메소드에 `@Query` 바로 작성
    // 1. 메소드 이름으로 만들던 방법에서는 파라미터가 많아지면 이름이 너무 복잡해지는 단점이 있었음
    // 2. 복잡한 `JPQL` 이 필요할 때 직접 넣어서 문제를 해결할 수도 있다.
    // 3. 굳이 실행을 안해도 애플리케이션 로딩 시점에 에러를 잡아준다. (애플리케이션 로딩 시점에 파싱을 하기 때문에)
    // 조금 복잡해진다 싶으면 `@Query`를 이용해주는 것이 효율적이다. `JPQL`로 풀어주면 된다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // 마치 객체를 생성해서 반환하는 것 같은 문법을 써야 Dto로 반환이 가능하다.
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);
}
