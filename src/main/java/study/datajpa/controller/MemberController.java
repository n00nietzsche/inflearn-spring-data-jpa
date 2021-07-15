package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        // 사실 이 기능을 권장하진 않는다.
        // 외부에 PK를 공개해서 좋을 이유도 많이 없고,
        // 이렇게 단순한 경우 자체도 많이 없다.

        // 받은 엔티티는 조회용으로만 사용해야 한다.
        // 트랜잭션이 없기 때문에 변경해봐야 적용 안된다.
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        Member member = new Member("김똘똘");
        memberRepository.save(member);
    }
}
