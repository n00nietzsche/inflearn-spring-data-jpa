package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager entityManager;

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

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        // 삭제 후 카운트 검증
        long countAfterDelete = memberRepository.count();
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("AAA", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 10);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void findTop3() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("AAA", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findTop3ByOrderByAgeDesc();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void namedQuery() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(10);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(10);
    }
    
    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 10, null);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() {
        Team t1 = new Team("ATeam");
        Team t2 = new Team("BTeam");

        teamRepository.save(t1);
        teamRepository.save(t2);

        Member m1 = new Member("AAA", 10, t1);
        Member m2 = new Member("BBB", 10, t2);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println(dto);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 10, null);
        Member m3 = new Member("CCC", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        List<String> names = Arrays.asList("AAA", "BBB", "CCC");
        List<Member> byNames = memberRepository.findByNames(names);

        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10, null);
        Member m2 = new Member("BBB", 10, null);
        Member m3 = new Member("CCC", 20, null);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        List<Member> members = memberRepository.findMembersByUsername("");
        assertThat(members).isNotEqualTo(null);

        Member member = memberRepository.findMemberByUsername("");
        assertThat(member).isEqualTo(null);
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));
        memberRepository.save(new Member("member6", 10, null));

        int age = 10;
        int limit = 3;

        // 주의! 스프링 데이터 JPA는 페이지를 0부터 시작한다.
        // 쿼리의 조건이 복잡하면 나중에는 `Sort.by()`가 제대로 안 풀릴 수 있으니 주의하자.
        // 사실 단순히 앞에꺼 3개 조회는 `.findTop3()` 가 가장 효율적이다.
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));


        Page<Member> memberPage = memberRepository.findByAge(age, pageRequest);
        // 참고로 여기 페이징에서 찾은 `Member`도 물론 API 에서 엔티티 그대로 내리면 절대 안된다.
        // 무조건 DTO로 필요한 정보만 빼서 내려야 한다.
        // 아래와 같은 방식으로 할 수 있다.
        Page<MemberDto> map = memberPage.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        List<Member> content = memberPage.getContent();

        assertThat(content.size()).isEqualTo(limit);
        assertThat(memberPage.getTotalElements()).isEqualTo(6);
        assertThat(pageRequest.getPageNumber()).isEqualTo(0);
        assertThat(memberPage.getTotalPages()).isEqualTo(2);
        assertThat(memberPage.isFirst()).isEqualTo(true);
        assertThat(memberPage.hasNext()).isEqualTo(true);
    }

    @Test
    public void pagingSlice() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));
        memberRepository.save(new Member("member6", 10, null));

        int age = 10;
        int limit = 3;

        // 주의! 스프링 데이터 JPA는 페이지를 0부터 시작한다.
        // `Slice`는 처음부터 `N+1`을 요청해서 다음 페이지가 있는지 확인한다
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));

        // `Page`가 `Slice`를 상속하는 타입이기 때문에, `Page`로 `Slice`가 받아지니 주의하자.
        // 설계상 `Slice`를 상속받아 전체 `limit`을 -1하고 전체 개수를 추가로 조회하면 `Page`가 된다.
        Slice<Member> memberPage = memberRepository.findSliceByAge(age, pageRequest);
        List<Member> content = memberPage.getContent();

        assertThat(content.size()).isEqualTo(limit);
        assertThat(pageRequest.getPageNumber()).isEqualTo(0);
        assertThat(memberPage.isFirst()).isEqualTo(true);
        assertThat(memberPage.hasNext()).isEqualTo(true);
    }

    @Test
    public void pagingList() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));
        memberRepository.save(new Member("member6", 10, null));

        int age = 10;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));
        // 그냥 limit, offset, 정렬(`PageRequest`)만 이용하고 싶다면 `List`로 반환하면 된다.
        List<Member> memberPage = memberRepository.findListByAge(age, pageRequest);

        for (Member member : memberPage) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void pagingListSplitCount() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));
        memberRepository.save(new Member("member6", 10, null));

        int age = 10;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> memberPage = memberRepository.findSplitCountByAge(age, pageRequest);
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 15, null));
        memberRepository.save(new Member("member3", 17, null));
        memberRepository.save(new Member("member4", 40, null));
        Member member5Original = new Member("member5", 31, null);
        memberRepository.save(member5Original);

        int resultCount = memberRepository.bulkAgePlus(20);

        // Entity가 변경된 것이 아니라서 1차캐시에 남아있던 `member5`는 여전히 `age`가 `31`인 상태이다.
        Member member5 = memberRepository.findByUsername("member5").get(0);

        // 메모리에 올라간 엔티티는 DB데이터의 변경사항을 모르기 때문에 이 결과는 31이 나온다.
        // System.out.println("member51.getAge() = " + member5Original.getAge());

        assertThat(member5.getAge()).isEqualTo(32);
        assertThat(resultCount).isEqualTo(2);
    }
}
