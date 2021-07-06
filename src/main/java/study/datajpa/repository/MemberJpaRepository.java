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
}
