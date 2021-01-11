package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {
    // 실무에서는 가급적 Setter 사용 안함
    @Id @GeneratedValue
    private Long id;
    private String userName;

    // 엔티티는 기본 생성자가 하나 있어야 한다.
    // 아무데서나 쓰이지 않게, protected 로 만든다.
    // JPA의 프록시 기술 때문임
    protected Member() {

    }

    public Member(String userName) {
        this.userName = userName;
    }
}
