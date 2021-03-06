package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

// Ctrl + P 를 누르면 넣어야 하는 것들의 타입이 나온다.
// T extends Object, ID extends Object
// 각각 엔티티와 기본키의 타입을 넣어준다.

// 인터페이스고 구현 코드가 없는데 어떻게 실행되는 걸까?

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

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

    List<Member> findMembersByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalMemberByUsername(String username); // 단건

    // 페이징 쿼리 등이 표준화되면서 개발자는 더욱 더 비즈니스에 관련된 복잡한 쿼리에만 집중할 수 있게 되었다.
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);
    List<Member> findListByAge(int age, Pageable pageable);
    // `totalCount`를 매기는데 불필요한 조인을 사용함으로써 성능저하가 발생하는 것에 대한 대책으로 나온 방법
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findSplitCountByAge(int age, Pageable pageable);

    // `@Modifying`을 빼면 에러가 나는데, `invalid data access`, `not supported DML` 등의 에러가 난다.
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // JPA의 벌크성 API는 주의점이 있다.
    // 기본적으로 JPA는 자바 객체 엔티티를 기반으로 지속적인 동기화를 통해 여러가지 변화가 이루어진다.
    // 그런데 벌크성 API는 바로 DB에 날아가는 것이기 때문에, 이러한 기본 매커니즘을 위배하는 행동이다.
    // 그래서 언제나 엔티티를 수정하는 것이 아니라, DB 데이터를 직접 수정한다는 것을 머릿속에 새겨둬야 한다.
    // `@Modifying`의 `clearAutomatically`를 `true`로 만들어주면, 실수를 방지하는데 도움이 된다.


    // `스프링 데이터 JPA`는 `join fetch`를 이용하려면 매번 `JPQL`을 직접 입력해줘야할까?
    //   -> 엔티티 그래프를 이용해 해결하자.
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = "team")
    // `스프링 데이터 JPA`를 이용한 `join fetch` 가능
    // 내부적으로 `join fetch`를 쓴다.
    List<Member> findAll();

    // 사용자 정의 쿼리에 `@EntityGraph`를 해도 `join fetch`가 됨
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);

    // 이렇게 세팅하면, JPA가 제공하는 쿼리 힌트를 이용할 수 있다.
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @QueryHints(value = {@QueryHint(name = "org.hibernate.readOnly", value = "true")},
                forCounting = true)
    List<Member> findPageByUsername(String username, Pageable pageable);


    // JPA에서 LOCK을 지원한다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    // `Generic`으로 타입을 받아서 동적으로 타입을 결정하는 것을 동적 `Projection`이라고 한다.
    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);
}
