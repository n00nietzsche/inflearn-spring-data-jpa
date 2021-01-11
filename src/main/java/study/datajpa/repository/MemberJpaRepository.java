package study.datajpa.repository;

import study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

/*
프로젝트 구조에서 Repository, Entity 등의 위치는 항상 내 메인 Application 이 있는 곳의 하위여야 한다.
나의 경우 study.datajpa 밖에 만들었다가 에러가 났었다.
 */

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    // SpringBoot Container 가 알아서 영속성 매니저를 가져옴
    private final EntityManager entityManager;

    public Member save(Member member){
        entityManager.persist(member);
        return member;
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }
}
