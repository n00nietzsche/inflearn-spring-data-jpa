package study.datajpa.repository;

import study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/*
프로젝트 구조에서 Repository, Entity 등의 위치는 항상 내 메인 Application 이 있는 곳의 하위여야 한다.
나의 경우 study.datajpa 밖에 만들었다가 에러가 났었다.
 */

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    // SpringBoot Container 가 알아서 영속성 매니저를 가져옴
    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        // SQL 문처럼 찝기 위해서는 JPQL 을 사용해야 함
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        // `member`가 `null`일 수도 있다는 뜻
        // java 8에 있는 기본적인 기능이다.
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {

        return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        // 엔티티에 `@NamedQuery`로 정의해뒀던 것을 불러올 수 있음.
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    // offset -> 몇 번째부터
    // limit -> 몇 개를 가져와
    // DB가 바뀌어도 Dialect 에 의해 DB 벤더에 맞는 쿼리가 나감
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    // 보통 페이징에서 몇번째 페이지인지 알기 위해 가져오게 돼있음
    // 여기서는 당연히 쿼리에 `sorting`이 필요 없음
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }
}
