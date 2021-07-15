package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final ModelMapper modelMapper;
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

    @GetMapping("/members")
    // `@PageableDefault`를 이용해서 페이징 기본값들 설정 가능
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "age", direction = Sort.Direction.DESC) Pageable pageable) {
        // 어떤 find든 뒤에 pageable 만 넘겨주면 가능하다.
        // 메소드 이름으로 쿼리 생성한 기타 메소드도 가능하다. ex) findByUsername...
        // PageRequest 라는 객체를 이용하는 것이기 때문에, PageRequest 객체를 잘 보면 여러가지 기능을 사용할 수 있다.
        // http://localhost:8080/members?page=0&size=3&sort=id,desc&sort=username,desc
        // `Entity`를 `DTO`로 바꾸는 방법은 현재까지 발견한 방법 중에는 아래의 방법이 가장 편한 것 같다.
        return memberRepository
                .findAll(pageable)
                .map(member -> modelMapper.map(member, MemberDto.class));
    }

    @PostConstruct
    public void init() {
        for(int i=0; i<100; i++){
            Member member = new Member("김똘똘"+i, i);
            memberRepository.save(member);
        }
    }
}
