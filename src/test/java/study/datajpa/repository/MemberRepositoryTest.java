package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import study.datajpa.entity.Member;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    void printMemberRepository() {
        // class com.sun.proxy.$Proxy95
        // 스프링 데이터 JPA가 구현 클래스를 만들어서 꽂아버림
        System.out.println("memberRepository = " + memberRepository.getClass());
    }

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // JAVA 8에서 제공하는 Optional 로 제공한다.
        // 원래 그냥 get 으로 가져오는 것이 좋은 방법은 아님
        // orElse 로 원래 값이 없을 때 어떻게 행동할지 정해주는 것이 바람직함
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
}
