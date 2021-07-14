package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
// 엔티티는 기본 생성자가 하나 있어야 한다.
// 아무데서나 쓰이지 않게, protected 로 만든다.
// JPA의 프록시 기술 때문임
// 리플렉션으로 객체를 만들기 위해서
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// `@ToString` 에 연관관계 필드 찍으면 무한참조될 수 있으니까 조심하기
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        // 관례상 이렇게 준 것이며, 관례를 따르지 않아도 에러가 나거나 하진 않음.
        // XML에 작성하는 방법도 있긴 함
        // 애초에 실무에서 잘 쓰지 않는 기능이긴 함
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {
    // 실무에서는 가급적 Setter 사용 안함
    @Id @GeneratedValue
    // 관례상 테이블명_id 라는 이름을 컬럼 이름으로 많이 쓰기 때문에 설정해줌
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    // N 쪽에 foreign key 가 들어가기 때문에 `@JoinColumn` 해주기
    // `@ManyToOne` 관계는 `FetchType`의 기본 전략이 `Eager`로 되어 있는데 꼭 `LAZY`로 바꾸어주자
    // `LAZY`가 아닌 경우 예상치 못한 쿼리가 나가서 성능 최적화가 매우 힘들 수 있다
    // 관련 링크: https://velog.io/@jakeseo_me/%EC%9E%90%EB%B0%94-ORM-%ED%91%9C%EC%A4%80-JPA-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D-%EA%B8%B0%EB%B3%B8%ED%8E%B8-28-%EC%A6%89%EC%8B%9C-%EB%A1%9C%EB%94%A9%EA%B3%BC-%EC%A7%80%EC%97%B0-%EB%A1%9C%EB%94%A9
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if(team != null){
            this.team = team;
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        // 팀에 있는 멤버에도 추가를 해주어야 함.
        // 이 부분이 실무에서 좀 개인적으로 깜빡할 때가 있었음
        team.getMembers().add(this);
    }
}
