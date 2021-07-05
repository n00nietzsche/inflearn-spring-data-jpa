package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    // JPA의 `EntityManager`를 가져오기 위한 애노테이션
    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team member = em.find(Team.class, id);
        return Optional.ofNullable(member);
    }

    public Long count() {
        // JPQL에서는 `count`와 같이 SQL에서 제공하는 기본적인 함수들 제공함
        return em.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }
}
