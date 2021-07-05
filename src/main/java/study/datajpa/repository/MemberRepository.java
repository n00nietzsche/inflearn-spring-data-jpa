package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

// Ctrl + P 를 누르면 넣어야 하는 것들의 타입이 나온다.
// T extends Object, ID extends Object
// 각각 엔티티와 기본키의 타입을 넣어준다.

// 인터페이스고 구현 코드가 없는데 어떻게 실행되는 걸까?
public interface MemberRepository extends JpaRepository<Member, Long> {
}
