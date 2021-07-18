package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    // 이렇게 표현식(SpEL을 쓰는 것을 open projection 이라고 한다.
    // 이렇게 애노테이션을 붙이면, 모든 것을 조회한 다음에 정제해서 내보내주는 것이다.
    // close projection 은 애노테이션 없이 쓰면 된다. 조금 더 최적화가 된다.
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
