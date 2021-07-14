package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
// 이름은 `MemberRepositoryImpl`과 같이 `리포지토리 이름` + `Impl`을 맞추어주어야 JPA에서  잘 찾는다.
// 바꿀려면 규칙을 바꿀 수도 있긴 하다.
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    // 여기서 예를들어 JDBC 템플릿을 쓰고싶다면, 데이터베이스 커넥션을 얻어서 쓰면 된다.

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
